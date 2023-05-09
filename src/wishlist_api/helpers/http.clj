(ns wishlist-api.helpers.http)


(def status-code
  {:ok 200
   :created 201
   :bad-request 400
   :unauthorized 401
   :forbidden 403
   :not-found 404
   :internal 500})
