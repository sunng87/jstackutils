(defproject info.sunng/jstackutils "0.1.0-SNAPSHOT"
  :description "A set of utils for using jstack"
  :url "https://github.com/sunng87/jstackutils"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :repl-options {:init-ns jstackutils.core}

  :profiles {:cli {:main ^:skip-aot jstackutils.main
                   :dependencies [[org.clojure/tools.cli "1.0.194"]]
                   :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
                   ;; NOTE add graalvm path env GRAALVM_HOME when running native-image
                   :native-image {:jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                                  :opts ["--no-server"
                                         "--no-fallback"
                                         "--initialize-at-build-time"]
                                  :name "jstackutils"}}}
             :uberjar {:aot :all})
