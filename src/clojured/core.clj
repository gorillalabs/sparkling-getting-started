(ns clojured.core
  (:require  [sparkling.core :as spark]
             [sparkling.conf :as s-conf]
             [sparkling.destructuring :as s-de]
             [clj-time.format :as tf]
             [clojure.pprint :refer [pprint]]
             ))

(defonce filename "access.log")

(def master "local[*]")
(def conf {})
(def env {
          "spark.executor.memory" "4G",
          "spark.files.overwrite" "true"
          })

(defn line-count [lines]
  (->> lines
       count))

(defn process [f]
  (with-open [rdr (clojure.java.io/reader filename)]
    (let [result (f (line-seq rdr))]
      (if (seq? result)
        (doall result)
        result))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn- new-spark-context []
  (let [c (-> (s-conf/spark-conf)
              (s-conf/master master)
              (s-conf/app-name "sparkling")
              (s-conf/set "spark.akka.timeout" "300")
              (s-conf/set conf)
              (s-conf/set-executor-env env))]
    (spark/spark-context c) ))


(defn line-count* [lines]
  (->> lines
       spark/count))



(defonce sc (delay (new-spark-context)))

(defn process* [f]
  (let [lines-rdd (spark/text-file @sc filename)]
    (f lines-rdd)))



#_
(process line-count)

#_
(process* line-count*)
#_
(clojure.java.browse/browse-url "http://localhost:4040/")


(defn -main [& args]
  (println (process* line-count*)))
