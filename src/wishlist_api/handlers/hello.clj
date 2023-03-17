(ns wishlist-api.handlers.hello)


(defn hello-world
  [_]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World!"})
