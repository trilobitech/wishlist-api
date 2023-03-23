(ns wishlist-api.handlers.login
  (:require
    [wishlist-api.helpers.utils :refer [call-handler]]))


(defn ^:private login-with-email-code
  [_]
  {:status  204
   :body    ""})


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
