(ns wishlist-api.config
  (:require
    [environ.core :refer [env]]))


(def env-mode (env :env-mode "prod"))

(def is-debug? (= env-mode "dev"))

(def http-port (Integer/parseInt (env :port "3000")))


(def db-config
  (case env-mode
    "dev" {:server-type :dev-local
           :system "dev"
           :storage-dir "/var/lib/datomic/storage"}
    "test" {:server-type :dev-local
            :system "dev"
            :storage-dir :mem}
    "prod" nil))
