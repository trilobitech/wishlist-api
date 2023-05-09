(ns wishlist-api.handlers.auth.token
  (:require
    [clojure.core.strint :refer [<<]]
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.domain.token-refresh :refer [token-refresh]]
    [wishlist-api.domain.validate-code :refer [validate-code]]
    [wishlist-api.helpers.http :refer [status-code]]))


(defn ^:private login-handler
  [grant_type]
  (case grant_type
    "validate_code" validate-code
    "refresh_token" token-refresh
    (throw+ {:type :bad-request
             :message (<< (if grant_type
                            "Field grant_type is required"
                            "Invalid grant_type: ~{grant_type}"))
             :domain :application})))


(defn ^:private call-handler
  [args]
  (-> args
      :grant_type
      login-handler
      (apply [args])))


(defn auth-token
  [context]
  (->> context
       :json-params
       call-handler
       (assoc {}
              :status (status-code :created)
              :body)))
