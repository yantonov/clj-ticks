(defproject clj-ticks "0.1.0-SNAPSHOT"
  :description "convert c# ticks to date and vise versa"
  :url "https://github.com/yantonov/ticks"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]]

  :plugins
  [[lein-cljsbuild "1.1.2"]]

  :cljsbuild
  {:builds
   {:production
    {:source-paths ["src"]
     :compiler
     {:output-to "target/ticks.js"
      :source-map "target/ticks.js.map"
      :optimizations :advanced
      :pretty-print false
      :parallel-build true}}

    :unittest
    {:source-paths ["src" "test"]
     :compiler
     {:output-to "target/testable-ticks.js"
      :optimizations :whitespace
      :pretty-print false
      :parallel-build true}
     :notify-command ["phantomjs"
                      "phantom/unit-test.js"
                      "phantom/unit-test.html"]}}

   :test-commands {"unit-tests"
                   ["phantomjs"
                    "phantom/unit-test.js"
                    "phantom/unit-test.html"]}})
