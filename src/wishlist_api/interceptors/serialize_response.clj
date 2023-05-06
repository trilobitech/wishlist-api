(ns wishlist-api.interceptors.serialize-response
  (:require
    [clojure.data.json :as json]
    [io.pedestal.interceptor.helpers :as interceptor]))


(def interceptor->response-serializer
  (interceptor/on-response
    ::serialize-response
    (fn [response]
      (let [{:keys [headers body]} response
            new-headers (assoc headers "Content-Type" "application/json")
            new-body (if (map? body) (json/write-str body) body)]
        (assoc response
               :headers new-headers
               :body    new-body)))))
