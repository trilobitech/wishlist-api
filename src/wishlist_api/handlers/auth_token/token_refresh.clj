(ns wishlist-api.handlers.auth-token.token-refresh
  (:require
    [slingshot.slingshot :refer [throw+ try+]]
    [wishlist-api.handlers.auth-token.common :refer [generate-token]]
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


(defn token-refresh
  [{:keys [refresh_token]}]
  {:status (status-code :created)
   :body (-> refresh_token
             get-refresh-token-data
             (select-keys [:user_id :user_email])
             generate-token)})
