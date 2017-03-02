(defproject gorillalabs/sparkling-getting-started "2.0.0-SNAPSHOT"
            :description "A Sample project demonstrating the use of Sparkling (https://gorillalabs.github.io/sparkling/), as shown in tutorial https://gorillalabs.github.io/sparkling/articles/tfidf_guide.html"
            :url "https://gorillalabs.github.io/sparkling/articles/tfidf_guide.html"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.8.0"]
                           [gorillalabs/sparkling "2.0.0"]]

            :aot [#".*" sparkling.serialization sparkling.destructuring]
            :main tf-idf.core
            :profiles {:provided {:dependencies [[org.apache.spark/spark-core_2.10 "2.1.0"]
                                                 [org.apache.spark/spark-sql_2.10 "2.1.0"]]}
                       :dev {:plugins [[lein-dotenv "RELEASE"]]}})


;; run example with
;;     lein run

;; test with
;;     lein test
