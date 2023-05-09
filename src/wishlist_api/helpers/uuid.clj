(ns wishlist-api.helpers.uuid)


(defn uuid
  ([] (java.util.UUID/randomUUID))
  ([uuid-str] (try
                (java.util.UUID/fromString uuid-str)
                (catch Exception _ nil))))
