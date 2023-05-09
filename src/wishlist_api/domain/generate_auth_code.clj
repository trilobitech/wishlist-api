(ns wishlist-api.domain.generate-auth-code
  (:require
    [clj-time.coerce :as time-coerce]
    [clj-time.core :as time]
    [clojure.core.strint :refer [<<]]
    [clojure.string :refer [blank? join]]
    [clojure.tools.logging :as logging]
    [slingshot.slingshot :refer [throw+]]
    [wishlist-api.helpers.crypto :refer [encrypt-json]]
    [wishlist-api.helpers.hash :refer [sha1-str]]
    [wishlist-api.helpers.validators :refer [valid-email?]]))


(defn ^:private email-code-template
  [{:keys [code]}]
  (str "Olá, aqui está o seu código de acesso: " code))


(defn ^:private send-email
  #_{:clj-kondo/ignore [:unused-binding]}
  [destination content]
  ;; TODO: implement email sending
  (logging/debug (<< "Sending email…\nDestination: ~{destination}\nContent: ~{content}")))


(defn ^:private send-email-code
  [email code]
  (let [content (email-code-template {:code code})]
    (send-email email content)))


(defn ^:private generate-code
  ([lenght] (join (take lenght (repeatedly #(rand-int 10)))))
  ([] (generate-code 6)))


(defn ^:private generate-code-for-email
  [email]
  (let [code (generate-code)]
    {:email (sha1-str email)
     :code code
     :created-at (time-coerce/to-long (time/now))}))


(defn ^:private validate-email
  [email]
  (cond
    (valid-email? email) email
    (blank? email) (throw+ {:type :bad-request
                            :message "Field email is required"
                            :domain :application})
    :else (throw+ {:type :bad-request
                   :message (<< "Invalid email: ~{email}")
                   :domain :application})))


(defn generate-auth-code
  [{:keys [email]}]
  (let [data (->>
               email
               validate-email
               generate-code-for-email)
        vault (encrypt-json data)
        code (:code data)]
    (send-email-code email code)
    {:vault vault
     :code code}))
