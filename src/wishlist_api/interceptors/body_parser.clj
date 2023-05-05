(ns wishlist-api.interceptors.body-parser
  (:require
    [io.pedestal.http.body-params :refer [body-params]]))


(def interceptor->parse-request-body (body-params))
