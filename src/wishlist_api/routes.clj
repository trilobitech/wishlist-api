(ns wishlist-api.routes
  (:require
    [wishlist-api.handlers.graphql :refer [graphql]]
    [wishlist-api.handlers.login :refer [login]]))


(defn routes
  []
  #{["/auth/token" :post login :route-name :login]
    ["/graphql" :post graphql :route-name :graphql]})
