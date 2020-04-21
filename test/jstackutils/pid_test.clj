(ns jstackutils.pid-test
  (:require [clojure.test :refer :all]
            [jstackutils.pid :as pid]))

(deftest test-pid
  (testing "get pid of current process"
    (is (> (pid/pid) 0))))

(deftest test-override-pid
  (testing "set custom pid when defeault pid detection not working"
    (pid/set-pid! 100)
    (is (= (pid/pid) 100))))
