(defproject gorillalabs/sparkling-getting-started "1.0.0-SNAPSHOT"
            :description "A Clojure Library for Apache Spark"
            :url "https://github.com/chrisbetz/sparkling"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [gorillalabs/sparkling "1.0.0"]
                           [org.clojure/tools.logging "0.3.1"]
                           [org.apache.spark/spark-core_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer]]
                           ]
            :aot :all
            :main sparkling.example.tfidf

            :profiles {:gorilla {:dependencies [[gorilla-renderable "1.0.0"]
                                                [cider/cider-nrepl "0.8.2"]
                                                [org.clojure/tools.nrepl "0.2.5"]
                                                [gorilla-repl "0.3.4" :exclusions [org.clojure/clojure
                                                                                   http-kit
                                                                                   ring/ring-json
                                                                                   cheshire
                                                                                   compojure
                                                                                   org.slf4j/slf4j-api
                                                                                   ch.qos.logback/logback-classic
                                                                                   gorilla-renderable
                                                                                   org.clojure/data.codec
                                                                                   javax.servlet/servlet-api
                                                                                   grimradical/clj-semver
                                                                                   clojure-complete]
                                                 ]]
                                 :plugins      [[lein-gorilla "0.3.4"]]
                                 :repl-options {:nrepl-handler sparkling.nrepl/handler}}}
            :javac-options ["-Xlint:unchecked" "-source" "1.6" "-target" "1.6"]
            :jvm-opts ^:replace ["-server" "-Xmx1g"]
            :global-vars {*warn-on-reflection* false}
            )

;; run example with
;;     lein run




;; First: Start an nrepl-server
;; lein do clean, with-profile +gorilla repl :headless :port 9001

;; Then, start a gorilla repl
;; lein with-profile +gorilla gorilla :nrepl-port 9001  :port 9000


;; and open the Gorilla REPL in your browser
;; open http://127.0.0.1:9000/worksheet.html?filename=easy-report
