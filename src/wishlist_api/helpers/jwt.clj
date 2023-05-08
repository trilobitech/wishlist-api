(ns wishlist-api.helpers.jwt
  (:require
    [buddy.sign.jwt :as jwt]
    [clj-time.core :as time]))


(def ^:private secret (or (System/getenv "JWT_SECRET") "secret"))


(defn sign
  [type payload expiration]
  (jwt/sign
    (assoc payload :exp (time/plus (time/now) expiration))
    (str type secret)))


(defn unsign
  [type token]
  (try (jwt/unsign token (str type secret))
       (catch clojure.lang.ExceptionInfo ex
         (let [error-data (ex-data ex)]
           (throw (if (= :validation (:type error-data))
                    (case (:cause error-data)
                      :exp
                      (ex-info "Token expired"
                               {:type :token-validation
                                :cause :exp
                                :message "Token expired"}
                               ex)
                      :nbf
                      (ex-info "Token not valid yet"
                               {:type :token-validation
                                :cause :nbf
                                :message "Token not valid yet"})
                      (:signature :header)
                      (ex-info "Invalid token"
                               {:type :token-validation
                                :cause :invalid
                                :message "Invalid token"}
                               ex)
                      ex)
                    ex))))))
