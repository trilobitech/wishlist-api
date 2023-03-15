(ns wishlist-api.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all])
  (:gen-class))


; Simple Body Page
(defn hello-world [_] ;(3)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World!"})


; Our main routes
(defroutes app-routes
  (GET "/" [] hello-world)
  (route/not-found "Error, page not found!"))


; Our main entry function
(defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http://127.0.0.1:" port "/"))))
