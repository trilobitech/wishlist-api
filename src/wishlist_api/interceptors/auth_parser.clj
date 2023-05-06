(ns wishlist-api.interceptors.auth-parser
  (:require
    [clojure.string :as str]
    [io.pedestal.interceptor.helpers :as interceptor]
    [wishlist-api.helpers.jwt :refer [unsign]]))


(def interceptor->parse-auth-header
  (interceptor/on-request
    ::auth-parser
    (fn [request]
      (let [authorization-header (get-in request [:headers "authorization"])
            token (if (string? authorization-header)
                    (str/replace (str/trim authorization-header) #"(?i)^bearer\s+(\S+)$" "$1")
                    nil)
            auth-data (if token (unsign token) nil)]
        (assoc request :auth-data auth-data)))))
