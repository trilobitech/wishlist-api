(ns wishlist-api.helpers.utils)


(defn call-handler
  [handler-adapter context]
  (let [handler (handler-adapter context)]
    (handler context)))
