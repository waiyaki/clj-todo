(defproject webdev "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring "1.7.1"]
                 [compojure "1.6.1"]
                 [org.clojure/java.jdbc "0.7.9"]
                 [org.postgresql/postgresql "42.2.6"]]
  :main ^:skip-aot webdev.core
  :target-path "target/%s"
  :min-lein-version "2.0.0"
  :uberjar-name "webdev.jar"
  :profiles {:uberjar {:aot :all}
             :dev     {:source-paths ["src" "dev"]
                       :main         webdev.dev}})
