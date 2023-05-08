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
      (throw+ {:type :token-validation
               :cause :exp
               :message "Token expired"}))
    (catch [:cause :nbf] _
      (throw+ {:type :token-validation
               :cause :nbf
               :message "Token not valid yet"}))
    (catch Object {:keys [cause]}
      (throw+ {:type :token-validation
               :cause cause
               :message "Invalid token"}))))
