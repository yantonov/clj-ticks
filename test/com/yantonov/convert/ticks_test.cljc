(ns com.yantonov.convert.ticks-test
  (:require [com.yantonov.convert.ticks :as ticks]
            [com.yantonov.convert.utils :as utils]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is are]])))

(deftest ticks-test
  (are [y m d expected-ticks]
      (= expected-ticks (ticks/ticks y m d))
    1    1  1   0
    2    1  1   315360000000000
    5    1  1   1262304000000000
    1900 1  1   599266080000000000
    1900 12 31  599580576000000000
    1910 1  1   602421408000000000
    1991 9  1   628192800000000000
    2012 4  1   634688352000000000))

(deftest ticks-precise-test
  (are [y m d hh mm ss ms expected-ticks]
      (= expected-ticks (ticks/ticks y m d hh mm ss ms))
    2013 2 23 23 46 37 918 634972599979180000))

(deftest to-datetime-test
  (are [y m d]
      (let [[actual-y actual-m actual-d hh mm ss ms]
            (ticks/to-datetime (ticks/ticks y m d))]
        (= [y m d] [actual-y actual-m actual-d]))
    2014 6 1
    1 1 1
    5 1 1
    1970 1 1
    1970 12 31
    1900 1  1
    1900 12 31
    1910 1  1
    1991 9  1
    2012 4  1))

(deftest to-calendar-test
  (are [y m d]
      (let [t
            (ticks/ticks y m d)

            date-time
            (ticks/to-calendar t)

            [actual-y actual-m actual-d]
            (utils/to-date-parts date-time)]
        (= [y m d] [actual-y actual-m actual-d]))
    2014 6 1
    #?(:clj 1
       :cljs 1901) 1 1
    #?(:clj 5
       :cljs 1905) 1 1
    1970 1 1
    1970 12 31
    1900 1  1
    1900 12 31
    1910 1  1
    1991 9  1
    2012 4  1))

(deftest to-datetime-test
  (are [h m s ms]
      (let [tick (ticks/ticks 2014 6 1 h m s ms)
            [_ _ _
             actual-h actual-m actual-s actual-ms] (ticks/to-datetime tick)]
        (and (= actual-h h)
             (= actual-m m)
             (= actual-s s)
             (= actual-ms ms)))
    23 59 59 999
    0 0 0 0
    1 0 0 0
    0 59 59 999
    12 34 56 789))
