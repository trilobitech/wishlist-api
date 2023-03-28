(ns wishlist-api.helpers.hash)


(defn sha1-str
  ;; https://gist.github.com/hozumi/1472865
  [text]
  (->> (-> "sha1"
           java.security.MessageDigest/getInstance
           (.digest (.getBytes text)))
       (map #(.substring
               (Integer/toString
                 (+ (bit-and % 0xff) 0x100) 16) 1))
       (apply str)))
