(ns wishlist-api.core
  (:gen-class)
  (:require
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]
    [wishlist-api.routes :refer [routes]]))


(defn create-server
  []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (http/create-server
      {::http/routes (route/expand-routes (routes))
       ::http/type   :jetty
       ::http/port   port
       ::http/host   "0.0.0.0"})))


(defn -main
  [& _]
  (http/start (create-server)))
