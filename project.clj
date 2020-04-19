(defproject info.sunng/jstackutils "0.1.0-SNAPSHOT"
  :description "A set of utils for using jstack"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :repl-options {:init-ns jstackutils.core}

  :profiles {:cli {:main ^:skip-aot jstackutils.main
                   :dependencies [[org.clojure/tools.cli "1.0.194"]]}
             :uberjar {:aot :all}})
