(ns wishlist-api.handlers.auth-token.common
  (:require
    [clj-time.core :as time]
    [wishlist-api.helpers.jwt :as jwt]))


;; access token valid for 1 hour
(def ^:private access-token-duration-time (time/hours 1))


;; refresh token valid for 1 month
(def ^:private refresh-token-duration-time (time/months 1))


(defn generate-token
  ([id email]
   (generate-token {:user_id id :user_email email}))
  ([token-data]
   {:token_type "Bearer"
    :access_token (jwt/sign :access-token token-data access-token-duration-time)
    :refresh_token (jwt/sign :refresh-token token-data refresh-token-duration-time)
    :expires_in (time/in-seconds access-token-duration-time)}))
