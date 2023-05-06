(ns wishlist-api.interceptors.debug-headers
  (:require
    [io.pedestal.interceptor.helpers :as interceptor]
    [wishlist-api.config :refer [is-debug?]]))


(def interceptor->maybe-remove-debug-headers
  (interceptor/on-response
    ::debug-headers
    (fn [response]
      (if is-debug?
        response
        (let [headers (:headers response {})
              headers-keys (keys headers)
              debug-headers (filter #(re-find #"(?i)^x-debug-" %) headers-keys)
              new-headers (apply dissoc headers debug-headers)]
          (assoc response :headers new-headers))))))
