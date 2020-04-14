(ns jstackutils.core
  (:require [jstackutils.pid :as pid]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.java.shell :as shell])
  (:import [java.text SimpleDateFormat]
           [java.util Date]))

(defn- time-now []
  (let [default-format (SimpleDateFormat. "yyyyMMddHHmmss")]
    (.format default-format (Date.))))

(defn- split-by-newline [jstack-output]
  (string/split jstack-output #"\n"))

(defn- join-by-newline [seqs]
  (string/join seqs "\n"))

(defn- split-by-double-newlines [jstack-output]
  (string/split jstack-output #"\n\n"))

(defn- join-by-double-newlines [seqs]
  (string/join seqs "\n\n"))

(defn- split-stack-trace [jstack-output]
  (mapv split-by-newline (split-by-double-newlines jstack-output)))

(defn- join-stack-trace [jstack-output-seqs]
  (join-by-double-newlines (map join-by-newline jstack-output-seqs)))

(defn- is-thread-stacktrace [jstack-output-seq-item]
  (string/starts-with? (first jstack-output-seq-item) "\""))

(defn jstack [output-prefix & {:keys [filters]}]
  (let [file-full-name (str output-prefix "-" (time-now))
        out-file (io/file file-full-name)]
    (let [output (:out (shell/sh "jstack" (str (pid/pid))))]
      (spit out-file output))))

(defn with-min-interval [last-call-time-atom
                         f
                         min-interval-ms]
  (when (> (- (System/currentTimeMillis) @last-call-time-atom)
           min-interval-ms)
    (locking last-call-time-atom
      (when (> (- (System/currentTimeMillis) @last-call-time-atom)
               min-interval-ms)
        (f)
        (reset! last-call-time-atom (System/currentTimeMillis))))))

(defn thread-prefix-filter [thread-prefix]
  (fn [jstack-output]
    ;; TODO: impl
    ))
