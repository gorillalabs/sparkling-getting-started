(defproject gorillalabs/sparkling-getting-started "1.0.0-SNAPSHOT"
            :description "A Clojure Library for Apache Spark"
            :url "https://github.com/chrisbetz/sparkling"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [gorillalabs/sparkling "1.0.0-SNAPSHOT"]
                           [org.clojure/tools.logging "0.3.1"]


                           [org.apache.spark/spark-core_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer]]
                           [org.apache.spark/spark-streaming_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer com.fasterxml.jackson.core/jackson-databind]]
                           [org.apache.spark/spark-streaming-kafka_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer com.fasterxml.jackson.core/jackson-databind]]
                           [org.apache.spark/spark-sql_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer org.scala-lang/scala-compiler com.fasterxml.jackson.core/jackson-databind]]

                           ;; [AVRO Feature] This adds support for reading avro files
                           [org.apache.avro/avro "1.7.6"]
                           [org.apache.avro/avro-mapred "1.7.6" :exclusions [org.slf4j/slf4j-log4j12 org.mortbay.jetty/servlet-api com.thoughtworks.paranamer/paranamer io.netty/netty commons-lang]]
                           ;; [/AVRO Feature]

                           ]
            :aot :all
            :main sparkling.example.tfidf

            :javac-options ["-Xlint:unchecked" "-source" "1.6" "-target" "1.6"]
            :jvm-opts ^:replace ["-server" "-Xmx1g"]
            :global-vars {*warn-on-reflection* false}
            )

;; test with
;;     lein do clean, test

;; run example with
;;     lein run


