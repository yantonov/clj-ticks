(ns com.test-runner
  (:require [cljs.test :refer-macros [run-tests]]
            [com.yantonov.convert.ticks-test]
            [com.yantonov.convert.utils-test]))

(enable-console-print!)

(defn runner []
  (if (cljs.test/successful?
       (run-tests
        'com.yantonov.convert.ticks-test
        'com.yantonov.convert.utils-test))
    0
    1))
