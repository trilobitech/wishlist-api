(ns wishlist-api.domain.user-interactor
  (:require
    [com.walmartlabs.lacinia.resolve :refer [resolve-as]]
    [slingshot.slingshot :refer [try+]]
    [wishlist-api.config :refer [is-debug?]]
    [wishlist-api.data.datasources.user-datasource :as ds]
    [wishlist-api.domain.invalidate-token :refer [invalidate-user-tokens]]))


(defn know-me
  [context args _]
  (try+
    (let [email (-> context :auth-data :user_email)
          data (assoc args :email email)
          result (ds/user->insert data)]
      (invalidate-user-tokens result)
      result)
    (catch [:cognitect.anomalies/category :cognitect.anomalies/conflict] _
      (resolve-as nil {:message "User already exists, please use `changeMe` instead."
                       :type :conflict
                       :domain :application}))))


(defn change-me
  [context args _]
  (let [user-id (get-in context [:auth-data :user_id])]
    (cond
      user-id
      (ds/user->update {:id user-id
                        :name (:name args)
                        :email (:email args)})
      :else
      (resolve-as nil {:message "User does not exist yet, please use `createUser` first."
                       :type :not-found
                       :domain :application}))))


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
        (resolve-as nil {:type :not-implemented
                         :message "This query is not implemented yet."
                         :domain :application})
        :else
        (ds/user->find-all)))
