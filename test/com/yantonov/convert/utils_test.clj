(ns com.yantonov.convert.utils-test
  (:require [com.yantonov.convert.utils :refer :all]
            [clojure.test :refer :all]))

(deftest is-leap-test
  (are [year verdict]
      (= verdict (is-leap? year))
    1900 false
    1904 true
    1984 true
    2000 true))

(deftest day-of-year-test
  (are [year month day index]
      (= index (day-of-year year month day))
    2000 1 1 0
    2000 1 2 1
    2000 3 1 (+ 31 29)
    1999 12 31 (dec (reduce + [31 28 31 30 31 30 31 31 30 31 30 31]))))

(deftest absolute-day-index-test
  (are [year month day index]
      (= index (absolute-day-index year month day))
    1 1 1 0
    1 1 2 1
    1 12 31 (dec (reduce + [31 28 31 30 31 30 31 31 30 31 30 31]))
    5 1 1 (inc (* 4 365))))

(deftest day-index-from-ticks-test
  (are [ticks day-index]
      (= day-index (day-index-from-ticks ticks))
    0 0
    (dec ticks-per-day) 0
    ticks-per-day 1
    (dec (* 2 ticks-per-day)) 1
    (* 2 ticks-per-day) 2))

(deftest year-from-day-index-test
  (are [day-index expected-year]
      (= expected-year (year-from-day-index day-index))
    0 1
    364 1
    365 2
    366 2
    (+ 1 (* 365 4)) 5
    (absolute-day-index 2014 6 1) 2014))
