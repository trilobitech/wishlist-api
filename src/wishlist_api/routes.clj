(ns wishlist-api.routes
  (:require
    [wishlist-api.handlers.auth-code :refer [auth-code]]
    [wishlist-api.handlers.auth-token.grant-handler :refer [auth-token]]
    [wishlist-api.handlers.graphql :refer [graphql]]))


(defn routes
  []
  #{["/auth/code" :post auth-code :route-name :request-auth-code]
    ["/auth/token" :post auth-token :route-name :auth-token]
    ["/graphql" :post graphql :route-name :graphql]})
