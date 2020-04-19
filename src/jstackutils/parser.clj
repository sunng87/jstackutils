(ns jstackutils.parser
  (:require [clojure.string :as string]))

(defn- split-by-newline [jstack-output]
  (string/split jstack-output #"\n"))

(defn- join-by-newline [seqs]
  (string/join "\n" seqs))

(defn- split-by-double-newlines [jstack-output]
  (string/split jstack-output #"\n\n"))

(defn- join-by-double-newlines [seqs]
  (string/join "\n\n" seqs))

(defn parse-stack-trace [jstack-output]
  (mapv split-by-newline (split-by-double-newlines jstack-output)))

(defn dump-stack-trace [jstack-output-seqs]
  (join-by-double-newlines (map join-by-newline jstack-output-seqs)))

(defn is-thread-stacktrace [jstack-output-seq-item]
  (string/starts-with? (first jstack-output-seq-item) "\""))
