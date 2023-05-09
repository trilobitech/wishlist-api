(ns wishlist-api.resolvers.user-resolver
  (:require
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.config :refer [is-debug?]]
    [wishlist-api.data.datasources.user-datasource :as ds]))


(defn know-me
  [context args _]
  (ds/user->insert {:name args
                    :email (get-in context [:auth-data :user_email])}))


(defn change-me
  [context args _]
  (ds/user->update {:id (get-in context [:auth-data :user_id])
                    :name (:name args)
                    :email (:email args)}))


(defn who-am-i
  [context _ _]
  (ds/user->find-by-email (get-in context [:auth-data :user_email])))


(defn who-is-it
  [context args _]
  (let [id (:id args)
        my-id (get-in context [:auth-data :user_id])
        result (ds/user->find-by-id id)]
    (if (not= id my-id)
      (dissoc result :email)
      result)))


(defn all-of-us
  [_ _ _]
  (cond (not is-debug?)
        (throw+ {:type :forbidden
                 :message "You are not authorized to perform this action"
                 :domain :application}))
  (ds/user->find-all))
