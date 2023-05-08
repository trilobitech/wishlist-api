(ns wishlist-api.handlers.auth-code
  (:require
    [clj-time.coerce :as time-coerce]
    [clj-time.core :as time]
    [clojure.string :refer [blank? join]]
    [wishlist-api.helpers.crypto :refer [encrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


(defn ^:private email-code-template
  [{:keys [code]}]
  (str "Olá, aqui está o seu código de acesso: " code))


(defn ^:private send-email
  [destination content]
  ;; TODO: implement email sending
  (println "Sending email to:" destination)
  (println content))


(defn ^:private send-email-code
  [email code]
  (let [content (email-code-template {:code code})]
    (send-email email content)))


(defn ^:private generate-code
  ([lenght] (join (take lenght (repeatedly #(rand-int 10)))))
  ([] (generate-code 6)))


(defn ^:private generate-code-for-email
  [email]
  (let [code (generate-code)]
    {:email (sha1-str email)
     :code code
     :created-at (time-coerce/to-long (time/now))}))


(defn ^:private validate-email
  [email]
  (cond
    (valid-email? email) email
    (blank? email) (throw (IllegalArgumentException. "Email is required"))
    :else (throw (IllegalArgumentException. (str "Invalid email: " email)))))


(defn ^:private generate-auth-code
  [{:keys [email]}]
  (let [data (->>
               email
               validate-email
               generate-code-for-email)
        vault (encrypt-json data)
        code (:code data)]
    (send-email-code email code)
    {:status 201
     :headers {"X-Debug-Email-Code" code}
     :body {:vault vault}}))


(defn auth-code
  [context]
  (-> context
      :json-params
      generate-auth-code))
