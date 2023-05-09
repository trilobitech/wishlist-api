(ns wishlist-api.interceptors.error-handler
  (:require
    [clojure.stacktrace :refer [print-stack-trace]]
    [io.pedestal.interceptor.error :refer [error-dispatch]]
    [wishlist-api.helpers.http :refer [status-code]]))


(defn internal-error
  [context ex]
  (print-stack-trace ex)
  (println "Internal server error")
  (assoc context :response
         {:status  (:internal status-code)
          :body    {:error-message "Internal server error"}}))


(defn build-error-response
  [context err-data]
  (let [message (:message err-data)
        status (get status-code (:type err-data) (status-code :internal))]
    (assoc context :response
           {:status  status
            :body    {:error-message message}})))


(def interceptor->error-handler
  (error-dispatch
    [context ex]
    [{:domain :application}]
    (build-error-response context (ex-data ex))

    :else
    ;; (assoc context ::interceptor.chain/error ex)))
    (internal-error context ex)))
