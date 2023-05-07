(ns wishlist-api.handlers.graphql
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia :refer [execute]]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia.util :as util]))


(defn wishlist-schema
  []
  (-> (io/resource "wishlist-schema.edn")
      slurp
      edn/read-string
      (util/inject-resolvers {})
      schema/compile))


(def compiled-schema (wishlist-schema))


(defn graphql
  [context]
  (let [body-params (:json-params context)
        result (execute
                 compiled-schema
                 (:query body-params)
                 (:variables body-params)
                 (:operationName body-params))]
    {:status 200
     :body result}))
