(ns wishlist-api.core
  (:gen-class)
  (:require
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]
    [wishlist-api.interceptors.body-parser :refer [interceptor->parse-request-body]]
    [wishlist-api.interceptors.error-handler :refer [interceptor->error-handler]]
    [wishlist-api.interceptors.serialize-response :refer [interceptor->response-serializer]]
    [wishlist-api.routes :refer [routes]]))


(def ^:private http-routes (route/expand-routes (routes)))


(def ^:private http-port (Integer/parseInt (or (System/getenv "PORT") "3000")))


(defn ^:private server-config
  []
  {::http/routes http-routes
   ::http/type   :jetty
   ::http/host   "0.0.0.0"
   ::http/port   http-port})


(defn ^:private create-server
  []
  (->
    (server-config)
    (http/default-interceptors)
    (update ::http/interceptors conj
            interceptor->parse-request-body
            interceptor->response-serializer
            interceptor->error-handler)
    (http/create-server)))


(defn -main
  [& _]
  (http/start (create-server)))
