(ns wishlist-api.helpers.jwt
  (:require
    [buddy.sign.jwt :as jwt]
    [clj-time.core :as time]))


(def ^:private secret (or (System/getenv "JWT_SECRET") "secret"))


(defn sign
  [payload expiration]
  (jwt/sign
    (assoc payload :exp (time/plus (time/now) expiration))
    secret))
