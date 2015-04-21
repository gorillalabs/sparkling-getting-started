(defproject gorillalabs/sparkling-getting-started "1.0.0-SNAPSHOT"
            :description "A Sample project demonstrating the use of Sparkling (https://gorillalabs.github.io/sparkling/), as shown in tutorial https://gorillalabs.github.io/sparkling/articles/tfidf_guide.html"
            :url "https://gorillalabs.github.io/sparkling/articles/tfidf_guide.html"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [gorillalabs/sparkling "1.2.1-SNAPSHOT"]]

            :aot [#".*" sparkling.serialization]
            :main tf-idf.core
            :profiles {:provided {:dependencies [[org.apache.spark/spark-core_2.10 "1.3.1"]]}
                       :dev {:plugins [[lein-dotenv "RELEASE"]]}})


;; run example with
;;     lein run

;; test with
;;     lein test
