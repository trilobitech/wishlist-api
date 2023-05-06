(ns wishlist-api.interceptors.error-handler
  (:require
    [clojure.stacktrace :refer [print-stack-trace]]
    [io.pedestal.interceptor.error :refer [error-dispatch]]))


(defn internal-error
  [context ex]
  (print-stack-trace ex)
  (assoc context :response
         {:status  500
          :body    {:error-message "Internal server error"}}))


(def interceptor->error-handler
  (error-dispatch
    [context ex]
    [{:exception-type :java.lang.IllegalArgumentException}]
    (assoc context :response
           {:status  400
            :body    {:error-message (.getMessage (ex-cause ex))}})

    [{:type :http-error}]
    (assoc context :response
           {:status  (:status (ex-data ex))
            :body    {:error-message (:message (ex-data ex))}})

    :else
    ;; (assoc context ::interceptor.chain/error ex)))
    (internal-error context ex)))
