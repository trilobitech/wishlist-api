(ns wishlist-api.handlers.login
  (:require
    [clojure.string :refer [blank? join]]
    [wishlist-api.helpers.crypto :refer [decrypt-json encrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


;; code valid for 5 minutes
(def ^:private code-duration-time (* 5 60 1000))


(defn ^:private now
  []
  (System/currentTimeMillis))


(defn generate-token
  []
  {:token "token" :refresh_token "refresh_token"})


(defn generate-code
  ([lenght] (join (take lenght (repeatedly #(rand-int 10)))))
  ([] (generate-code 6)))


(defn generate-code-for-email
  [email]
  (let [code (generate-code)]
    (println (str "code: " code))
    {:email (sha1-str email)
     :code code
     :created-at (now)}))


(defn ^:private validate-email
  [email]
  (cond
    (valid-email? email) email
    (blank? email) (throw (IllegalArgumentException. "Email is required"))
    :else (throw (IllegalArgumentException. (str "Invalid email: " email)))))


(defn ^:private login-with-email-code
  [{:keys [email]}]
  (let [vault (->>
                email
                validate-email
                generate-code-for-email
                encrypt-json)]
    {:status 201
     :body {:vault vault}}))


(defn check-email-code
  [{:keys [vault email code]}]
  (let [data (decrypt-json vault)]
    (cond
      (not= (sha1-str email) (:email data)) (throw (IllegalArgumentException. "Email invalid"))
      (not= code (:code data)) (throw (IllegalArgumentException. "Code invalid"))
      (> (now) (+ (:created-at data) code-duration-time)) (throw (IllegalArgumentException. "Code expired"))
      :else {:status 201 :body (generate-token)})))


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
        method (:method body-params)
        handler (login-handler method)]
    (handler body-params)))
