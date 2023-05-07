(ns wishlist-api.config
  (:require
    [environ.core :refer [env]]))


(def is-debug? (= (env :debug-mode) "true"))

(def http-port (Integer/parseInt (env :port "3000")))
