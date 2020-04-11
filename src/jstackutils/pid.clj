(ns jstackutils.pid
  (:import [java.lang.management ManagementFactory]))

(defn- get-pid-from-jdk9-api []
  (let [clazz (Class/forName "java.lang.ProcessHandle")
        current-method (.getMethod clazz "current" (into-array Class []))
        current (.invoke current-method nil (into-array Object []))]
    (.pid current)))

(defn- try-get-pid-from-jdk9-api []
  (try
    (get-pid-from-jdk9-api)
    (catch Throwable _ nil)))

(defn- get-pid-from-jmx-bean []
  (let [mxbean (ManagementFactory/getRuntimeMXBean)
        mxbean-name (.getName mxbean)]
    (Long/parseLong (first (clojure.string/split mxbean-name #"@")))))

(def pid
  (or (try-get-pid-from-jdk9-api)
      (get-pid-from-jmx-bean)))
