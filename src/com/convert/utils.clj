(ns com.convert.utils)

(def tick 10000000) ;; single tick represents one hundred nanoseconds
(def ticks-per-minute (* 60 tick))
(def ticks-per-hour (* 60 ticks-per-minute))
(def month-length [31 28 31 30 31 30 31 31 30 31 30 31])
(def ticks-per-day (* 24 ticks-per-hour))

(defn is-leap?
  "Checks if given year is leap."
  [year]
  (cond
    (zero? (rem year 4))
    (cond
      (zero? (rem year 100))
      (zero? (rem year 400))
      :default true)
    :default false))

(defn day-of-year
  "Returns zero-based index of day from the beginning of year. Month and day are one-based"
  [year month day]
  (+ (reduce + (take (dec month) month-length))
     (if (and (> month 2) (is-leap? year))
       1
       0)
     (dec day)))

(defn absolute-day-index
  "Returns zero-based day index from 1 january 0001"
  [year month day]
  (let [prev-year (dec year)]
    (+ (* (- year 1) 365)
       (+ (quot prev-year 4)
          (- (quot prev-year 100))
          (quot prev-year 400))
       (day-of-year year month day))))

(defn day-index-from-ticks
  "Gets day index based on ticks"
  [ticks]
  (quot ticks ticks-per-day))

(defn year-from-day-index
  "Returns year from absolute day index"
  [day-index]
  (let [day-per-year 365]
    (loop [y 1 days day-index]
      (let [diff (+ day-per-year
                    (if (is-leap? y) 1 0))]
        (cond (< days diff)
              y
              true
              (recur (inc y) (- days diff)))))))

(defn date-from-ticks
  "retuns vector [year month day] based on ticks (month is one based)"
  [ticks]
  (let [day-index (day-index-from-ticks ticks)
        year (year-from-day-index day-index)
        prev-year (dec year)
        day-index-for-year (- day-index
                              (* 365 prev-year)
                              (quot prev-year 4)
                              (- (quot prev-year 100))
                              (quot prev-year 400))
        month-length (if (is-leap? year)
                       (assoc month-length 1 (inc (get month-length 1)))
                       month-length)
        [month day] (loop [month 0
                           day day-index-for-year]
                      (if (< day (get month-length month))
                        [(inc month) (inc day)]
                        (recur (inc month)
                               (- day (get month-length month)))))]
    [year month day]))

(defn time-from-ticks
  "returns vector [hour minute seconds milliseconds] based on ticks"
  [ticks]
  ;; TODO: macro for quote and rem by single call
  (let [ticks-for-time (rem ticks  ticks-per-day)
        h (quot ticks-for-time ticks-per-hour)
        rem-h (- ticks-for-time (* h ticks-per-hour))
        m (quot rem-h ticks-per-minute)
        rem-m (- rem-h (* m ticks-per-minute))
        s (quot rem-m tick)
        rem-s (- rem-m (* s tick))
        ms (quot rem-s (quot tick 1000))]
    [h m s ms]))
