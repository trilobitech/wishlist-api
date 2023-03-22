(ns wishlist-api.routes
  (:require
    [wishlist-api.handlers.hello :refer [hello-world]]
    [wishlist-api.handlers.login :refer [login]]))


(defn routes
  []
  #{["/"      :get  hello-world :route-name :greet]
    ["/login" :post login       :route-name :login]})
