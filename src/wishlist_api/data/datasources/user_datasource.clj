(ns wishlist-api.data.datasources.user-datasource
  (:require
    [datomic.client.api :as d]
    [wishlist-api.data.client :refer [conn]]
    [wishlist-api.helpers.uuid :refer [uuid]]))


(defn user->insert
  [user]
  (let [data {:db/id "users"
              :user/id (uuid)
              :user/name (:name user)
              :user/email (:email user)}
        tx-result (d/transact conn {:tx-data [data]})
        db-after (:db-after tx-result)
        temp-id (-> tx-result :tempids (get "users"))
        user (d/pull db-after '[*] temp-id)]
    {:id (:user/id user)
     :name (:user/name user)
     :email (:user/email user)}))


(defn user->update
  [user]
  (let [temp-id [:user/id (uuid (:id user))]
        data (->> {:db/id temp-id
                   :user/name (:name user)
                   :user/email (:email user)}
                  (filter second)
                  (into {}))
        tx-result (d/transact conn {:tx-data [data]})
        db-after (:db-after tx-result)
        user (d/pull db-after '[*] temp-id)]
    {:id (:user/id user)
     :name (:user/name user)
     :email (:user/email user)}))


(defn user->find-by-email
  [email]
  (let [db (d/db conn)
        query '[:find ?id ?name ?email
                :keys id name email
                :in $ ?email
                :where
                [?e :user/id       ?id]
                [?e :user/name   ?name]
                [?e :user/email ?email]]]
    (first (d/q {:query query :args [db email]}))))


(defn user->find-by-id
  [id]
  (let [db (d/db conn)
        query '[:find ?id ?name ?email
                :keys id name email
                :in $ ?id
                :where
                [?e :user/id       ?id]
                [?e :user/name   ?name]
                [?e :user/email ?email]]]
    (first (d/q {:query query :args [db (uuid id)]}))))


(defn user->find-by-id-or-email
  [id email]
  (if id
    (user->find-by-id id)
    (user->find-by-email email)))


(defn user->find-all
  []
  (let [db (d/db conn)
        query '[:find ?id ?name
                :keys id name
                :where
                [?e :user/id       ?id]
                [?e :user/name   ?name]]
        result (d/q {:query query :args [db]})]
    result))
