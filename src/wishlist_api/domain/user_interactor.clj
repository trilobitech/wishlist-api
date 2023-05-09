(ns wishlist-api.domain.user-interactor
  (:require
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.config :refer [is-debug?]]
    [wishlist-api.data.datasources.user-datasource :as ds]))


(defn know-me
  [context args _]
  (let [email (-> context :auth-data :user_email)
        data (assoc args :email email)
        result (ds/user->insert data)]
    result))


(defn change-me
  [context args _]
  (let [user-id (-> context :auth-data :user_id)]
    (cond
      (not user-id)
      (throw+ {:type :not-found
               :message "User does not exist yet"
               :domain :application}))
    (ds/user->update {:id user-id
                      :name (:name args)
                      :email (:email args)})))


(defn who-am-i
  [context _ _]
  (let [{:keys [user_id user_email]} (:auth-data context)]
    (ds/user->find-by-id-or-email user_id user_email)))


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
