(ns wishlist-api.server
  (:gen-class)
  (:require
    [io.pedestal.http :as server]
    [wishlist-api.service :as service]))


;; This is an adapted service map, that can be started and stopped
;; From the REPL you can call server/start and server/stop on this service
(defonce runnable-service (server/create-server service/service))


(defn -main
  "The entry-point for 'lein run'"
  [& _]
  (println "\nCreating your server...")
  (server/start runnable-service))
