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

(defn -main [& args]
  (try
    (let [options (cli/parse-opts args cli-options)]
      (let [pid (first (:arguments options))
            output-prefix (-> options :options :output)

            jstack-results (jstk/jstack :pid pid)]
        (if output-prefix
          (jstk/dump output-prefix jstack-results)
          (println (jstk/dump* jstack-results)))))
    (finally
      (shutdown-agents))))
