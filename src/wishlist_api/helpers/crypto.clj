(ns wishlist-api.helpers.crypto
  (:require
    [clojure.data.json :as json]))


(def default-keypair-size 842)


(defn ^:private keypair-generator
  [length]
  (doto (java.security.KeyPairGenerator/getInstance "RSA")
    (.initialize length)))


(defn generate-keypair
  [length]
  (assert (>= length 512) "RSA Key must be at least 512 bits long.")
  (.generateKeyPair (keypair-generator length)))


(def ^:private default-keypair (generate-keypair default-keypair-size))

(def ^:private default-public-key (.getPublic default-keypair))

(def ^:private default-private-key (.getPrivate default-keypair))


(defn decode64
  [str]
  (.decode (java.util.Base64/getDecoder) str))


(defn encode64
  [bytes]
  (.encodeToString (java.util.Base64/getEncoder) bytes))


(defn encrypt
  "Perform RSA public key encryption of the given message string.
   Returns a Base64-encoded string of the encrypted data."
  ([message] (encrypt message default-public-key))
  ([message public-key]
   (encode64
     (let [cipher (doto (javax.crypto.Cipher/getInstance "RSA/ECB/PKCS1Padding")
                    (.init javax.crypto.Cipher/ENCRYPT_MODE public-key))]
       (.doFinal cipher (.getBytes message))))))


(defn decrypt
  "Use an RSA private key to decrypt a Base64-encoded string
   of ciphertext."
  ([message] (decrypt message default-private-key))
  ([message private-key]
   (let [cipher (doto (javax.crypto.Cipher/getInstance "RSA/ECB/PKCS1Padding")
                  (.init javax.crypto.Cipher/DECRYPT_MODE private-key))]
     (->> message
          decode64
          (.doFinal cipher)
          (map char)
          (apply str)))))


(defn encrypt-json
  ([message] (encrypt-json message default-public-key))
  ([message public-key] (encrypt (json/write-str message) public-key)))


(defn decrypt-json
  ([message] (decrypt-json message default-private-key))
  ([message private-key] (json/read-str (decrypt message private-key) :key-fn keyword)))
