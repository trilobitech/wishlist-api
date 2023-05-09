(ns wishlist-api.data.client
  (:require
    [datomic.client.api :as d]
    [wishlist-api.config :refer [db-config]]
    [wishlist-api.data.schemas.user-schema :refer [user-schema]]))


(def ^:private client (d/client db-config))


(d/create-database client {:db-name "wishlist"})


(def conn (d/connect client {:db-name "wishlist"}))


(def ^:private all-schemas (concat user-schema []))


(d/transact conn {:tx-data all-schemas})
