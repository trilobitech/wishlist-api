(ns wishlist-api.handlers.login
  (:require
    [clojure.string :refer [blank?]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


(defn ^:private validate-email
  [email]
  (cond
    (valid-email? email) email
    (blank? email) (throw (IllegalArgumentException. "Email is required"))
    :else (throw (IllegalArgumentException. (str "Invalid email: " email)))))


(defn ^:private login-with-email-code
  [{:keys [email]}]
  (validate-email email)
  {:status 204})


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
