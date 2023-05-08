(ns wishlist-api.handlers.auth-token.validate-code
  (:require
    [clj-time.coerce :as time-coerce]
    [clj-time.core :as time]
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.handlers.auth-token.common :refer [generate-token]]
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
      (throw+ {:type :input-validation
               :message "Something went wrong, please request a new code"})

      (not= (sha1-str email) (:email data))
      (throw+ {:type :input-validation
               :message "Email invalid"})

      (not= code (:code data))
      (throw+ {:type :input-validation
               :message "Code invalid"})

      (is-code-expired? data)
      (throw+ {:type :input-validation
               :message "Verification code expired"})

      :else {:status 201
             :body (generate-token nil email)})))
