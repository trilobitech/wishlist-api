(defproject wishlist-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[io.pedestal/pedestal.service "0.5.10"]
                 [io.pedestal/pedestal.route   "0.5.10"]
                 [io.pedestal/pedestal.jetty   "0.5.10"]
                 [org.slf4j/slf4j-simple       "1.7.28"]
                 [org.clojure/data.json         "2.4.0"]
                 [buddy/buddy-sign            "3.4.333"]
                 [clj-time                     "0.15.2"]]
  :main ^:skip-aot wishlist-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
