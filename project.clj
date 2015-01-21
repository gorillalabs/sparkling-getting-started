(defproject gorillalabs/sparkling-getting-started "1.0.0-SNAPSHOT"
            :description "A demo project for using Spark from Clojure using gorillalabs/sparkling"
            :url "https://github.com/gorillalabs/sparkling-getting-started"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [gorillalabs/sparkling "1.1.0"]
                           [org.clojure/tools.logging "0.3.1"]
                           [org.apache.spark/spark-core_2.10 "1.1.0" :exclusions [com.thoughtworks.paranamer/paranamer]]

                           [clj-time "0.9.0"]

                           ]
            :aot :all
            :main clojured.core

            :javac-options ["-Xlint:unchecked" "-source" "1.6" "-target" "1.6"]
            :jvm-opts ^:replace ["-server" "-Xmx1g"]
            :global-vars {*warn-on-reflection* false}
            )

;; run example with
;;     lein run


