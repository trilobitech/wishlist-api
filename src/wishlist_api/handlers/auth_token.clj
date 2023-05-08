(ns wishlist-api.handlers.auth-token
  (:require
    [clj-time.coerce :as time-coerce]
    [clj-time.core :as time]
    [wishlist-api.helpers.crypto :refer [decrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]
    [wishlist-api.helpers.jwt :as jwt]))


;; verification code valid for 5 minutes
(def ^:private verification-code-duration-time (time/minutes 5))


;; access token valid for 1 hour
(def ^:private access-token-duration-time (time/hours 1))


;; refresh token valid for 1 month
(def ^:private refresh-token-duration-time (time/months 1))


(defn ^:private generate-token
  [id email]
  (let [token-data {:user_id id :user_email email}]
    {:token_type "Bearer"
     :access_token (jwt/sign :access-token token-data access-token-duration-time)
     :refresh_token (jwt/sign :refresh-token token-data refresh-token-duration-time)
     :expires_in (time/in-seconds access-token-duration-time)}))


(defn ^:private get-refresh-token-data
  [refresh-token]
  (or refresh-token
      (throw (IllegalArgumentException. "Field refresh_token is required")))
  (try (jwt/unsign :refresh-token refresh-token)
       (catch clojure.lang.ExceptionInfo ex
         (let [error-data (ex-data ex)]
           (throw (if (= :token-validation (:type error-data))
                    (IllegalArgumentException. "Invalid refresh_token")
                    ex))))))


(defn ^:private check-email-code
  [{:keys [vault email code]}]
  (let [data (decrypt-json vault)
        created-at (time-coerce/from-long (get data :created-at 0))
        expire-at (time/plus created-at verification-code-duration-time)]
    (cond
      (not= (sha1-str email) (:email data)) (throw (IllegalArgumentException. "Email invalid"))
      (not= code (:code data)) (throw (IllegalArgumentException. "Code invalid"))
      (time/after? (time/now) expire-at) (throw (IllegalArgumentException. "Verification code expired"))
      :else {:status 201 :body (generate-token nil email)})))


(defn ^:private token-refresh
  [{:keys [refresh_token]}]
  (let [data (get-refresh-token-data refresh_token)
        token (generate-token (:user_id data) (:user_email data))]
    {:status 201
     :body token}))


(defn ^:private login-handler
  [grant_type]
  (case grant_type
    "validate_code" check-email-code
    "refresh_token" token-refresh
    nil (throw (IllegalArgumentException. "Field grant_type is required"))
    (throw (IllegalArgumentException. (str "Invalid grant_type: " grant_type)))))


(defn auth-token
  [context]
  (let [body-params (:json-params context)
        grant_type (:grant_type body-params)
        handler (login-handler grant_type)]
    (handler body-params)))
