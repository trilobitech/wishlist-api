(ns server
  (:gen-class)
  (:require
    [io.pedestal.http :as server]
    [io.pedestal.http.route :as route]
    [ns-tracker.core :refer [ns-tracker]]
    [wishlist-api.service :as service]))


(defonce modified-namespaces
  (ns-tracker ["src" "dev"]))


(defn watch-routes-fn
  [routes]
  (fn []
    (doseq [ns-sym (modified-namespaces)]
      (require ns-sym :reload))
    (route/expand-routes routes)))


(defn create-server
  []
  (-> service/service ; start with production configuration
      (merge {:env :dev
              ;; do not block thread that starts web server
              ::server/join? false
              ;; Routes can be a function that resolve routes,
              ;;  we can use this to set the routes to be reloadable
              ::server/routes (watch-routes-fn (service/routes))
              ;; all origins are allowed in dev mode
              ::server/allowed-origins {:creds true :allowed-origins (constantly true)}
              ;; Content Security Policy (CSP) is mostly turned off in dev mode
              ::server/secure-headers {:content-security-policy-settings {:object-src "'none'"}}})
      ;; Wire up interceptor chains
      server/default-interceptors
      server/dev-interceptors
      server/create-server))


(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& _]
  (println "\nCreating your [DEV] server...")
  (server/start (create-server)))


(defn -main
  [& _]
  (run-dev))
