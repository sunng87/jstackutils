(ns jstackutils.core
  (:require [jstackutils.pid :as pid]
            [jstackutils.parser :as parser]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell])
  (:import [java.text SimpleDateFormat]
           [java.util Date]))

(defn- time-now []
  (let [default-format (SimpleDateFormat. "yyyyMMddHHmmss")]
    (.format default-format (Date.))))

(defn jstack [output-prefix & {:keys [filters]}]
  (let [file-full-name (str output-prefix "-" (time-now))
        out-file (io/file file-full-name)
        output (:out (shell/sh "jstack" (str (pid/pid))))

        parsed-output-seq (parser/parse-stack-trace output)

        output (parser/dump-stack-trace parsed-output-seq)]

    (spit out-file output)))

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
