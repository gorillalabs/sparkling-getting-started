(ns clojured.core
  (:require  [sparkling.core :as spark]
             [sparkling.conf :as s-conf]
             [sparkling.destructuring :as s-de]
             [clj-time.format :as tf]
             [clojure.java.shell :refer [sh]]
             [clojure.pprint :refer [pprint]]
             ))

(defonce filename "access.log")


(defn line-count [lines]
  (->> lines
       count))

(defn process [f]
  (with-open [rdr (clojure.java.io/reader filename)]
    (let [result (f (line-seq rdr))]
      (if (seq? result)
        (doall result)
        result))))

#_
(process line-count)


(defn -main [& args]
  (println (process line-count)))
