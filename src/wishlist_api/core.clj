(ns wishlist-api.core
  (:gen-class)
  (:require
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]))


;; Simple Body Page
(defn hello-world
  [_]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World!"})


;; Our main routes
(def routes
  (route/expand-routes
    #{["/" :get hello-world :route-name :greet]}))


(defn create-server
  []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (http/create-server
      {::http/routes routes
       ::http/type   :jetty
       ::http/port   port
       ::http/host   "0.0.0.0"})))


(defn -main
  [& _]
  (http/start (create-server)))
