(ns wishlist-api.interceptors.error-handler
  (:require
    [clojure.stacktrace :refer [print-stack-trace]]
    [io.pedestal.interceptor.error :refer [error-dispatch]]))


(defn internal-error
  [context ex]
  (print-stack-trace ex)
  (println "Internal server error")
  (assoc context :response
         {:status  500
          :body    {:error-message "Internal server error"}}))


(def interceptor->error-handler
  (error-dispatch
    [context ex]
    [{:type :input-validation}]
    (assoc context :response
           {:status  400
            :body    {:error-message (:message (ex-data ex))}})

    [{:type :token-validation}]
    (assoc context :response
           {:status  401
            :body    {:error-message (:message (ex-data ex))}})

    :else
    ;; (assoc context ::interceptor.chain/error ex)))
    (internal-error context ex)))
