(ns wishlist-api.handlers.login
  (:require
    [clojure.string :refer [blank? join]]
    [wishlist-api.helpers.crypto :refer [encrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


(defn generate-code
  ([lenght] (join (take lenght (repeatedly #(rand-int 10)))))
  ([] (generate-code 6)))


(defn generate-code-for-email
  [email]
  (let [code (generate-code)]
    (println (str "code: " code))
    {:email (sha1-str email)
     :code code
     :created-at (System/currentTimeMillis)}))


(defn ^:private validate-email
  [email]
  (cond
    (valid-email? email) email
    (blank? email) (throw (IllegalArgumentException. "Email is required"))
    :else (throw (IllegalArgumentException. (str "Invalid email: " email)))))


(defn ^:private login-with-email-code
  [{:keys [email]}]
  (let [check (->>
                email
                validate-email
                generate-code-for-email
                encrypt-json)]
    {:status 200
     :body {:check check}}))


(defn ^:private login-handler
  [method]
  (case method
    "email-code" login-with-email-code
    nil (throw (IllegalArgumentException. "Login method is required"))
    (throw (IllegalArgumentException. (str "Invalid login method: " method)))))


(defn login
  [context]
  (let [body-params (:json-params context)
        method (:method body-params)
        handler (login-handler method)]
    (handler body-params)))
