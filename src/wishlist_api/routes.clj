(ns wishlist-api.routes
  (:require
    [wishlist-api.handlers.login :refer [login]]))


(defn routes
  []
  #{["/login" :post login :route-name :login]})
