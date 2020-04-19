(ns jstackutils.filters
  (:require [clojure.string :as string]
            [jstackutils.parser :as parser]))

(defn thread-prefix-filter [thread-prefix]
  (let [prefix (str "\"" thread-prefix)]
    (fn [parsed-output-section]
      (let [is-trace (parser/is-thread-stacktrace parsed-output-section)]
        (if is-trace
          (string/starts-with? (first parsed-output-section) prefix)
          true)))))