(ns wishlist-api.core
  (:gen-class)
  (:require
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]
    [wishlist-api.config :as config]
    [wishlist-api.interceptors.auth-parser :refer [interceptor->parse-auth-header]]
    [wishlist-api.interceptors.body-parser :refer [interceptor->parse-request-body]]
    [wishlist-api.interceptors.debug-headers :refer [interceptor->maybe-remove-debug-headers]]
    [wishlist-api.interceptors.error-handler :refer [interceptor->error-handler]]
    [wishlist-api.interceptors.serialize-response :refer [interceptor->response-serializer]]
    [wishlist-api.routes :refer [routes]]))


(defn ^:private server-config
  []
  (let [http-routes (route/expand-routes (routes))]
    {::http/routes http-routes
     ::http/type   :jetty
     ::http/host   "0.0.0.0"
     ::http/port   config/http-port}))


(defn ^:private create-server
  []
  (-> (server-config)
      (http/default-interceptors)
      (update ::http/interceptors conj
              interceptor->response-serializer
              interceptor->maybe-remove-debug-headers
              interceptor->parse-request-body
              interceptor->error-handler
              interceptor->parse-auth-header)
      (http/create-server)))


(defn -main
  [& _]
  (http/start (create-server)))
