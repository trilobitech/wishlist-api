(ns wishlist-api.interceptors.serialize-response
  (:require
    [clojure.data.json :as json]
    [io.pedestal.interceptor.helpers :as interceptor]))


(def interceptor->response-serializer
  (interceptor/on-response
    ::serialize-response
    (fn [response]
      (assoc response
             :headers {"Content-Type" "application/json"}
             :body    (let [body (:body response)]
                        (if (map? body) (json/write-str body) body))))))
