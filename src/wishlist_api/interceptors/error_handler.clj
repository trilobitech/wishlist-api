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


(def interceptor->error-handler
  (error-dispatch
    [context ex]
    [{:domain :application}]
    (let [err-data (ex-data ex)
          message (:message err-data)
          status (get status-code (:type err-data) (status-code :internal))]
      (assoc context :response
             {:status  status
              :body    {:error-message message}}))

    :else
    ;; (assoc context ::interceptor.chain/error ex)))
    (internal-error context ex)))
