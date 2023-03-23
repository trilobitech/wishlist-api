(ns wishlist-api.handlers.login
  (:require
    [wishlist-api.helpers.utils :refer [call-handler]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


(defn ^:private login-with-email-code
  [{:keys [json-params]}]
  (let [email (:email json-params)]
    (if (valid-email? email)
      {:status  204
       :body    ""}
      (throw (IllegalArgumentException.
               (if email (str "Invalid email: " email) "Email is required"))))))


(defn ^:private login-handler
  [method]
  (case method
    "email-code" login-with-email-code
    nil (throw (IllegalArgumentException. "Login method is required"))
    (throw (IllegalArgumentException. (str "Invalid login method: " method)))))


(defn ^:private login-handler-from-request
  [{:keys [json-params]}]
  (-> json-params
      :method
      login-handler))


(defn login
  [context]
  (call-handler login-handler-from-request context))
