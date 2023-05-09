(ns wishlist-api.handlers.graphql
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia :refer [execute]]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia.util :as util]
    [wishlist-api.helpers.http :refer [status-code]]
    [wishlist-api.resolvers.user-resolver :as users]))


(def ^:private resolver-registry
  {:queries/me users/who-am-i
   :queries/userById users/who-is-it
   :queries/users users/all-of-us
   :mutations/createUser users/know-me
   :mutations/updateUser users/change-me})


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
        status (status-code (if (:errors result) :bad-request :ok))]
    {:status status
     :body result}))
