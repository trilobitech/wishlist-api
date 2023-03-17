(ns wishlist-api.routes
  (:require
    [wishlist-api.handlers.hello :refer [hello-world]]))


(defn routes
  []
  #{["/" :get hello-world :route-name :greet]})
