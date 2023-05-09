(defproject wishlist-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :main ^:skip-aot wishlist-api.core
  :target-path "target/%s"

  :plugins [[lein-environ "1.2.0"]]

  :dependencies [[io.pedestal/pedestal.service "0.5.10"]
                 [io.pedestal/pedestal.route   "0.5.10"]
                 [io.pedestal/pedestal.jetty   "0.5.10"]
                 [org.slf4j/slf4j-simple       "1.7.28"]
                 [org.clojure/data.json         "2.4.0"]
                 [buddy/buddy-sign            "3.4.333"]
                 [clj-time                     "0.15.2"]
                 [com.walmartlabs/lacinia       "1.2.1"]
                 [environ                       "1.2.0"]
                 [slingshot                    "0.12.2"]]

  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                       :dependencies [[com.datomic/client-cloud "1.0.123"]]}

             :dev {:repositories [["cognitect-dev-tools" {:url      "https://dev-tools.cognitect.com/maven/releases/"
                                                          :username :env
                                                          :password :env}]]
                   :dependencies [[com.datomic/dev-local "1.0.243"]]
                   :env {:env-mode "dev"}}
             :test {:dependencies [[nubank/matcher-combinators "3.8.5"]]
                    :env {:env-mode "test"}}})
