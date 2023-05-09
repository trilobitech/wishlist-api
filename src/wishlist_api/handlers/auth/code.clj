(ns wishlist-api.handlers.auth.code
  (:require
    [wishlist-api.domain.generate-auth-code :refer [generate-auth-code]]
    [wishlist-api.helpers.http :refer [status-code]]))


(defn auth-code
  [context]
  (->> context
       :json-params
       generate-auth-code
       (#(into {:status (status-code :created)
                :headers {"X-Debug-Email-Code" (:code %)}
                :body (select-keys % [:vault])}))))
