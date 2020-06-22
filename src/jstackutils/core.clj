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

(defn jstack [& {:keys [filters pid jstack-exec]
                 :or {}}]
  (let [pid (or pid (pid/pid))
        jstack-exec (or jstack-exec "jstack")
        result (shell/sh jstack-exec (str pid))]
    (if (> (:exit result) 0)
      (throw (IllegalStateException. ^String (:err result)))
      (let [output (:out result)
            parsed-output-seq (parser/parse-stack-trace output)]
        (if (not-empty filters)
          (let [pred (apply every-pred filters)]
            (filter pred parsed-output-seq))
          parsed-output-seq)))))

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

(defn dump [output-prefix jstack-output-seq]
  (let [file-full-name (str output-prefix "-" (time-now))
        out-file (io/file file-full-name)
        output (parser/dump-stack-trace jstack-output-seq)]
    (spit out-file output)))

(defn dump* [jstack-output-seq]
  (parser/dump-stack-trace jstack-output-seq))
