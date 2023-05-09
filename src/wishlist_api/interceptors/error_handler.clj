(ns wishlist-api.interceptors.error-handler
  (:require
    [clojure.tools.logging :as logging]
    [io.pedestal.interceptor.error :refer [error-dispatch]]
    [wishlist-api.helpers.http :refer [status-code status-message]]))


(defn build-error-response
  [context {:keys [type message]}]
  (let [status (get status-code type (status-code :internal))
        error-message (or message (status-message type))]
    (assoc context :response
           {:status  status
            :body    {:errors
                      [{:message error-message}]}})))


(defn internal-error
  [context ex]
  (logging/error "Internal server error" ex)
  (build-error-response context {:type :internal :message "Internal server error"}))


(def interceptor->error-handler
  (error-dispatch
    [context ex]
    [{:domain :application}]
    (build-error-response context (ex-data ex))

    :else
    ;; (assoc context ::interceptor.chain/error ex)))
    (internal-error context ex)))
