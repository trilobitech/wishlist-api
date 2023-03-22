(ns wishlist-api.handlers.login
  (:require
    [clojure.data.json :as json]
    [wishlist-api.helpers.utils :refer [call-handler]]))


(defn ^:private login-with-email-code
  [_]
  {:status  204
   :headers {"Content-Type" "application/json"}
   :body    ""})


(defn ^:private invalid-login-method
  [{:keys [json-params]}]
  {:status 400
   :headers {"Content-Type" "application/json"}
   :body (json/write-str {:error-message (str "Invalid login method: " (json-params :method))})})


(defn ^:private login-handler
  [method]
  (case method
    "email-code" login-with-email-code
    invalid-login-method))


(defn ^:private login-handler-from-request
  [{:keys [json-params]}]
  (-> json-params
      :method
      login-handler))


(defn login
  [context]
  (call-handler login-handler-from-request context))
