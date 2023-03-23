(ns wishlist-api.interceptors.error-handler
  (:require
    [io.pedestal.interceptor.chain :as interceptor.chain]
    [io.pedestal.interceptor.error :refer [error-dispatch]]))


(def interceptor->error-handler
  (error-dispatch
    [context ex]
    [{:exception-type :java.lang.IllegalArgumentException}]
    (assoc context :response
           {:status  400
            :body    {:error-message (.getMessage (ex-cause ex))}})

    :else
    (assoc context ::interceptor.chain/error ex)))
