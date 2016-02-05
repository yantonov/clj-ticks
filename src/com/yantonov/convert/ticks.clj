(ns com.yantonov.convert.ticks
  (:require [com.yantonov.convert.utils :as utils]))

(defn ticks
  "Returns number of 100 nanosecond intervals from 1 january 0001."
  ([]
   (ticks (java.time.LocalDateTime/now)))
  ([^java.time.LocalDateTime dateTime]
   (let [y (. dateTime getYear)
         m (. dateTime getMonth)
         d (. dateTime getDayOfMonth)
         h (. dateTime getHour)
         m (. dateTime getMinute)
         s (. dateTime getSeconds)
         ms (quot (. dateTime getNanoseconds) 1000)]
     (ticks y (inc m) d h m s ms)))
  ([year month day]
   (* (utils/absolute-day-index year month day) 24 60 60 utils/tick))
  ([year month day hour minute]
   (+ (ticks year month day)
      (* hour 60 60 utils/tick)
      (* minute 60 utils/tick)))
  ([year month day hour minute second]
   (+ (ticks year month day hour minute)
      (* second utils/tick)))
  ([year month day hour minute second millis]
   (+ (ticks year month day hour minute second)
      (* millis (quot utils/tick 1000)))))

(defn to-datetime
  "Converts ticks to gregorianCalendar"
  [ticks]
  (let [[y m d] (utils/date-from-ticks ticks)
        [hh mm ss ms] (utils/time-from-ticks ticks)]
    [y m d hh mm ss ms]))

(defn to-calendar
  "Convert ticks to calendar"
  [ticks]
  (let [[y m d hh mm ss ms] (to-datetime ticks)]
    (java.time.LocalDateTime/of y m d hh mm ss (* ms 1000))))
