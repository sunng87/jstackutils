(ns jstackutils.main
  (:require [clojure.tools.cli :as cli]
            [jstackutils.core :as jstk])
  (:gen-class))

(def cli-options
  [[nil nil "Java process pid"
    :id :pid
    :required true
    :parse-fn #(Long/parseLong %)]
   ["-o" "--output OUTPUT" "Output file prefix"
    :id :output]
   ["-h" "--help"]])

(defn- jstack-executable-from-env []
  (System/getenv "JSTACK_EXEC"))

(defn -main [& args]
  (try
    (let [options (cli/parse-opts args cli-options)
          pid (first (:arguments options))
          output-prefix (-> options :options :output)

          jstack-results (jstk/jstack :pid pid
                                      :jstack-exec (jstack-executable-from-env))]
      (if output-prefix
        (jstk/dump output-prefix jstack-results)
        (println (jstk/dump* jstack-results))))
    (finally
      (shutdown-agents))))
