(ns wishlist-api.handlers.auth-token.token-refresh
  (:require
    [slingshot.slingshot :refer [throw+ try+]]
    [wishlist-api.handlers.auth-token.common :refer [generate-token]]
    [wishlist-api.helpers.jwt :as jwt]))


(defn ^:private get-refresh-token-data
  [refresh-token]
  (try+
    (cond (not refresh-token)
          (throw+ {:type :input-validation
                   :message "Field refresh_token is required"}))
    (jwt/unsign :refresh-token refresh-token)
    #_{:clj-kondo/ignore [:unresolved-symbol]}
    (catch [:type :token-validation] _
      (throw+ {:type :token-validation
               :message "Invalid refresh_token"}))))


(defn token-refresh
  [{:keys [refresh_token]}]
  {:status 201
   :body (-> refresh_token
             get-refresh-token-data
             (select-keys [:user_id :user_email])
             generate-token)})
