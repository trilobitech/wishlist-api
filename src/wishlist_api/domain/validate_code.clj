(ns wishlist-api.domain.validate-code
  (:require
    [clj-time.coerce :as time-coerce]
    [clj-time.core :as time]
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.data.datasources.token-datasource :refer [generate-token-for-user]]
    [wishlist-api.data.datasources.user-datasource :refer [user->find-by-email]]
    [wishlist-api.helpers.crypto :refer [decrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]))


;; verification code valid for 5 minutes
(def ^:private verification-code-duration-time (time/minutes 5))


(defn ^:private is-code-expired?
  [{:keys [created-at]}]
  (let [expire-at (-> created-at
                      time-coerce/from-long
                      (time/plus verification-code-duration-time))]
    (time/after? (time/now) expire-at)))


(defn validate-code
  [{:keys [vault email code]}]
  (let [data (decrypt-json vault)]
    (cond
      (not data)
      (throw+ {:type :bad-request
               :message "Something went wrong, please request a new code"
               :domain :application})

      (not= (sha1-str email) (:email data))
      (throw+ {:type :bad-request
               :message "Email invalid"
               :domain :application})

      (not= code (:code data))
      (throw+ {:type :bad-request
               :message "Code invalid"
               :domain :application})

      (is-code-expired? data)
      (throw+ {:type :bad-request
               :message "Verification code expired"
               :domain :application})

      :else (-> email
                user->find-by-email
                (or {:email email})
                generate-token-for-user))))
