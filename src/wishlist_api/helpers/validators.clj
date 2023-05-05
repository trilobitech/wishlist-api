(ns wishlist-api.helpers.validators)


(defn valid-email?
  [email]
  (let [email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"]
    (and email (re-matches email-regex email))))
