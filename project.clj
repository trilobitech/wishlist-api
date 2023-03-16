(defproject wishlist-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 ;; Compojure - A basic routing library
                 [compojure "1.7.0"]
                 ;; Our Http library for client/server
                 [http-kit "2.6.0"]
                 ;; Ring defaults - for query params etc
                 [ring/ring-defaults "0.3.4"]
                 ;; Clojure data.JSON library
                 [org.clojure/data.json "2.4.0"]]
  :main ^:skip-aot wishlist-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
