(ns wishlist-api.service
  (:require
    [io.pedestal.http :as http]
    [wishlist-api.config :as config]
    [wishlist-api.handlers.auth.code :refer [auth-code]]
    [wishlist-api.handlers.auth.token :refer [auth-token]]
    [wishlist-api.handlers.graphql :refer [graphql]]
    [wishlist-api.interceptors.auth-parser :refer [interceptor->parse-auth-header]]
    [wishlist-api.interceptors.body-parser :refer [interceptor->parse-request-body]]
    [wishlist-api.interceptors.debug-headers :refer [interceptor->maybe-remove-debug-headers]]
    [wishlist-api.interceptors.error-handler :refer [interceptor->error-handler]]
    [wishlist-api.interceptors.serialize-response :refer [interceptor->response-serializer]]))


(defn common-interceptors
  [handler]
  (conj [interceptor->response-serializer
         interceptor->maybe-remove-debug-headers
         interceptor->parse-request-body
         interceptor->error-handler
         interceptor->parse-auth-header]
        handler))


(defn routes
  []
  #{["/auth/code" :post (common-interceptors auth-code) :route-name :request-auth-code]
    ["/auth/token" :post (common-interceptors auth-token) :route-name :auth-token]
    ["/graphql" :post (common-interceptors graphql) :route-name :graphql]})


(def service
  {:env :prod
   ;; You can bring your own non-default interceptors. Make
   ;; sure you include routing and set it up right for
   ;; dev-mode. If you do, many other keys for configuring
   ;; default interceptors will be ignored.
   ;; ::http/interceptors []
   ::http/routes (routes)

   ;; Uncomment next line to enable CORS support, add
   ;; string(s) specifying scheme, host and port for
   ;; allowed source(s):
   ;;
   ;; "http://localhost:8080"
   ;;
   ;; ::http/allowed-origins ["scheme://host:port"]

   ;; Tune the Secure Headers
   ;; and specifically the Content Security Policy appropriate to your service/application
   ;; For more information, see: https://content-security-policy.com/
   ;;   See also: https://github.com/pedestal/pedestal/issues/499
   ;; ::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
   ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
   ;;                                                          :frame-ancestors "'none'"}}

   ;; Root for resource interceptor that is available by default.
   ;; ::http/resource-path "/public"

   ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
   ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
   ::http/type :jetty
   ::http/host "0.0.0.0"
   ::http/port config/http-port
   ;; Options to pass to the container (Jetty)
   ::http/container-options {:h2c? true
                             :h2? false
                             ;; :keystore "test/hp/keystore.jks"
                             ;; :key-password "password"
                             ;; :ssl-port 8443
                             :ssl? false
                             ;; Alternatively, You can specify your own Jetty HTTPConfiguration
                             ;; via the `:io.pedestal.http.jetty/http-configuration` container option.
                             ;; :io.pedestal.http.jetty/http-configuration (org.eclipse.jetty.server.HttpConfiguration.)
                             }})
