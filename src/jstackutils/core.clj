(ns jstackutils.core
  (:require [jstackutils.pid :as pid]
            [clojure.java.io :as io]
            [clojure.java.shell :as shell])
  (:import [java.text SimpleDateFormat]
           [java.util Date]))

(defn- time-now []
  (let [default-format (SimpleDateFormat. "yyyyMMddHHmmss")]
    (.format default-format (Date.))))

(defn jstack [output-path output-name]
  (let [file-full-name (str output-name "-" (time-now))
        out-file (io/file output-path file-full-name)]
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
