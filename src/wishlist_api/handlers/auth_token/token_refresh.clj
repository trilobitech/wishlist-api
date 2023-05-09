(ns wishlist-api.handlers.auth-token.token-refresh
  (:require
    [slingshot.slingshot :refer [throw+ try+]]
    [wishlist-api.data.datasources.user-datasource :refer [user->find-by-email
                                                           user->find-by-id]]
    [wishlist-api.handlers.auth-token.common :refer [generate-token-for-user]]
    [wishlist-api.helpers.http :refer [status-code]]
    [wishlist-api.helpers.jwt :as jwt]))


(defn ^:private get-refresh-token-data
  [refresh-token]
  (try+
    (cond (not refresh-token)
          (throw+ {:type :bad-request
                   :message "Field refresh_token is required"
                   :domain :application}))
    (jwt/unsign :refresh-token refresh-token)
    #_{:clj-kondo/ignore [:unresolved-symbol]}
    (catch [:type :unauthorized] _
      (throw+ {:type :bad-request
               :message "Invalid refresh_token"
               :domain :application}))))


(defn ^:private user-from-token
  [{:keys [user_id user_email]}]
  (if user_id
    (user->find-by-id user_id)
    (user->find-by-email user_email)))


(defn token-refresh
  [{:keys [refresh_token]}]
  {:status (status-code :created)
   :body (-> refresh_token
             get-refresh-token-data
             user-from-token
             generate-token-for-user)})
