(ns wishlist-api.handlers.auth-token.grant-handler
  (:require
    [clojure.core.strint :refer [<<]]
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.handlers.auth-token.token-refresh :refer [token-refresh]]
    [wishlist-api.handlers.auth-token.validate-code :refer [validate-code]]))


(defn ^:private login-handler
  [grant_type]
  (case grant_type
    "validate_code" validate-code
    "refresh_token" token-refresh
    nil (throw+ {:type :input-validation
                 :message "Field grant_type is required"})
    (throw+ {:type :input-validation
             :message (<< "Invalid grant_type: ~{grant_type}")})))


(defn auth-token
  [context]
  (let [body-params (:json-params context)]
    (-> body-params
        :grant_type
        login-handler
        (apply [body-params]))))
