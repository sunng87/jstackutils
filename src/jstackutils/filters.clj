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

(defn package-filter [contains-package-prefix]
  (fn [parsed-output-section]
    (some #(string/index-of % contains-package-prefix)
          parsed-output-section)))

(defn thread-state-filter [state]
  (let [state (format "java.lang.Thread.State: %s" (string/upper-case state))]
    (fn [parsed-output-sections]
      (if-let [state-line (second parsed-output-sections)]
        (string/index-of state-line state)
        false))))
