(defproject gorillalabs/sparkling-getting-started "1.0.0-SNAPSHOT"
  :description "A Sample project demonstrating the use of Sparkling (https://gorillalabs.github.io/sparkling/), as shown in tutorial https://gorillalabs.github.io/sparkling/articles/tfidf_guide.html"
  :url "https://gorillalabs.github.io/sparkling/articles/tfidf_guide.html"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [gorillalabs/sparkling "1.2.4" :exclusions [[javax.servlet/servlet-api]]]]

  :aot [#".*" sparkling.serialization sparkling.destructuring]
  :main tf-idf.core
  :plugins [[lein-gorilla "0.3.6"]]
  :profiles {:provided {:dependencies
                        [[org.apache.spark/spark-core_2.10 "1.6.1"
                          ;need exclusions thanks to this issue
                          ;https://issues.apache.org/jira/browse/SPARK-1693
                          :exclusions [[javax.servlet/servlet-api]
                                       [org.eclipse.jetty.orbit/javax.servlet]]]
                         [org.apache.spark/spark-mllib_2.10 "1.6.1"
                          :exclusions [[javax.servlet/servlet-api]]]
                          ]}
             :dev {:plugins [[lein-dotenv "RELEASE"]]}})


;; run example with
;;     lein gorilla-repl :port 9000

;; test with
;;     lein test
