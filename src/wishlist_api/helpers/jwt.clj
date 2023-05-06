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


(defn unsign
  [token]
  (try (jwt/unsign token secret)
       (catch clojure.lang.ExceptionInfo ex
         (let [error-data (ex-data ex)]
           (throw (if (= error-data {:type :validation :cause :exp})
                    (ex-info "Token expired" {:type :http-error :status 401 :message "Token expired"} ex)
                    ex))))))
