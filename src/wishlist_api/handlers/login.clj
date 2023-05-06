(ns wishlist-api.handlers.login
  (:require
    [clj-time.core :as time]
    [clojure.string :refer [blank? join]]
    [wishlist-api.helpers.crypto :refer [decrypt-json encrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]
    [wishlist-api.helpers.jwt :refer [sign]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


;; code valid for 5 minutes
(def ^:private code-duration-time (* 5 60 1000))


(defn ^:private now
  []
  (System/currentTimeMillis))


(defn send-email
  [destination content]
  ;; TODO: implement email sending
  (println "Sending email to:" destination)
  (println content))


(defn ^:private generate-token
  [email]
  ;; TODO: add user id to token
  {:access_token (sign {:userEmail email} (time/hours 1))
   :refresh_token (sign {:userEmail email} (time/months 1))
   :type "bearer"})


(defn generate-code
  ([lenght] (join (take lenght (repeatedly #(rand-int 10)))))
  ([] (generate-code 6)))


(defn ^:private generate-code-for-email
  [email]
  (let [code (generate-code)]
    {:email (sha1-str email)
     :code code
     :created-at (now)}))


(defn email-code-template
  [{:keys [code]}]
  (str "Olá, aqui está o seu código de acesso: " code))


(defn ^:private send-email-code
  [email code]
  (let [content (email-code-template {:code code})]
    (send-email email content)))


(defn ^:private validate-email
  [email]
  (cond
    (valid-email? email) email
    (blank? email) (throw (IllegalArgumentException. "Email is required"))
    :else (throw (IllegalArgumentException. (str "Invalid email: " email)))))


(defn ^:private login-with-email-code
  [{:keys [email]}]
  (let [data (->>
               email
               validate-email
               generate-code-for-email)
        vault (encrypt-json data)]
    (send-email-code email (:code data))
    {:status 201
     :body {:vault vault}}))


(defn check-email-code
  [{:keys [vault email code]}]
  (let [data (decrypt-json vault)]
    (cond
      (not= (sha1-str email) (:email data)) (throw (IllegalArgumentException. "Email invalid"))
      (not= code (:code data)) (throw (IllegalArgumentException. "Code invalid"))
      (> (now) (+ (:created-at data) code-duration-time)) (throw (IllegalArgumentException. "Code expired"))
      :else {:status 201 :body (generate-token email)})))


(defn ^:private login-handler
  [method]
  (case method
    "get-email-code" login-with-email-code
    "check-email-code" check-email-code
    nil (throw (IllegalArgumentException. "Login method is required"))
    (throw (IllegalArgumentException. (str "Invalid login method: " method)))))


(defn login
  [context]
  (let [body-params (:json-params context)
        method (:grant_type body-params)
        handler (login-handler method)]
    (handler body-params)))
