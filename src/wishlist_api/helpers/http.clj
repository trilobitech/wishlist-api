(ns wishlist-api.helpers.http)


(def status-code
  {:ok 200
   :created 201
   :bad-request 400
   :unauthorized 401
   :forbidden 403
   :not-found 404
   :conflict 409
   :internal 500
   :not-implemented 501})


(def status-message
  {:ok "OK"
   :created "Created"
   :bad-request "Bad Request"
   :unauthorized "Unauthorized"
   :forbidden "Forbidden"
   :not-found "Not Found"
   :conflict "Conflict"
   :internal "Internal Server Error"
   :not-implemented "Not Implemented"})
