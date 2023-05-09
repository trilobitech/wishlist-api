(ns wishlist-api.domain.invalidate-token
  (:require
    [clojure.tools.logging :as logging]))


(defn invalidate-user-tokens
  [{:keys [email]}]
  ;; TODO: Implement token invalidation
  (logging/info (str "Invalidating token for user: " email)))
