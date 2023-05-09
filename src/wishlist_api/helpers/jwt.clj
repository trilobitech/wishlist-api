(ns wishlist-api.helpers.jwt
  (:require
    [buddy.sign.jwt :as jwt]
    [clj-time.core :as time]
    [slingshot.slingshot :refer [throw+ try+]]))


(def ^:private secret (or (System/getenv "JWT_SECRET") "secret"))


(defn sign
  [type payload expiration]
  (jwt/sign
    (assoc payload :exp (time/plus (time/now) expiration))
    (str type secret)))


(defn unsign
  [type token]
  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (try+
    (jwt/unsign token (str type secret))
    (catch [:cause :exp] _
      (throw+ {:type :unauthorized
               :cause :exp
               :message "Token expired"
               :domain :application}))
    (catch [:cause :nbf] _
      (throw+ {:type :unauthorized
               :cause :nbf
               :message "Token not valid yet"
               :domain :application}))
    (catch Object {:keys [cause]}
      (throw+ {:type :unauthorized
               :cause cause
               :message "Invalid token"
               :domain :application}))))
