(ns wishlist-api.handlers.graphql
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia :refer [execute]]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia.util :as util]))


(def ^:private resolver-registry
  {})


(defn ^:private wishlist-schema
  []
  (-> (io/resource "wishlist-schema.edn")
      slurp
      edn/read-string
      (util/inject-resolvers resolver-registry)
      schema/compile))


(def ^:private compiled-schema (wishlist-schema))


(defn graphql
  [context]
  (let [body-params (:json-params context)
        result (execute
                 compiled-schema
                 (:query body-params)
                 (:variables body-params)
                 context)
        status (if (:errors result) 400 200)]
    {:status status
     :body result}))
