(ns wishlist-api.data.client
  (:require
    [datomic.client.api :as d]
    [wishlist-api.data.schemas.user-schema :refer [user-schema]]))


(def ^:private client
  ;; TODO: use env vars
  (d/client {:server-type :dev-local
             :system "dev"
             :storage-dir "/var/lib/datomic/storage"}))


(d/create-database client {:db-name "wishlist"})


(def conn (d/connect client {:db-name "wishlist"}))


(def ^:private all-schemas (concat user-schema []))


(d/transact conn {:tx-data all-schemas})
