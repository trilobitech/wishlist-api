(ns wishlist-api.core
  (:gen-class)
  (:require
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]
    [wishlist-api.interceptors.auth-parser :refer [interceptor->parse-auth-header]]
    [wishlist-api.interceptors.body-parser :refer [interceptor->parse-request-body]]
    [wishlist-api.interceptors.debug-headers :refer [interceptor->maybe-remove-debug-headers]]
    [wishlist-api.interceptors.error-handler :refer [interceptor->error-handler]]
    [wishlist-api.interceptors.serialize-response :refer [interceptor->response-serializer]]
    [wishlist-api.routes :refer [routes]]))


(defn ^:private server-config
  []
  (let [http-routes (route/expand-routes (routes))
        http-port   (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    {::http/routes http-routes
     ::http/type   :jetty
     ::http/host   "0.0.0.0"
     ::http/port   http-port}))


(defn ^:private create-server
  []
  (-> (server-config)
      (http/default-interceptors)
      (update ::http/interceptors conj
              interceptor->parse-auth-header
              interceptor->parse-request-body
              interceptor->maybe-remove-debug-headers
              interceptor->response-serializer
              interceptor->error-handler)
      (http/create-server)))


(defn -main
  [& _]
  (http/start (create-server)))
