(ns clojured.core-test
  (:require [clojured.core :refer :all]
            [clojure.test :refer :all]
            [sparkling.conf :as conf]
            [sparkling.api :as spark])
  )

(defn test-conf []
  (-> (conf/spark-conf)
      (conf/master "local")
      (conf/app-name "clojured-test")))

(deftest test-line-count*
  (let [conf (test-conf)]
    (spark/with-context
      sc conf
      (testing
        "no lines return 0"
        (is (= 0 (line-count* (spark/parallelize sc [])))))

      (testing
        "a single line returns 1"
        (is (= 1 (line-count* (spark/parallelize sc ["this is a single line"])))))

      (testing
        "multiple lines count correctly"
        (is (= 10 (line-count* (spark/parallelize sc (repeat 10 "this is a single line"))))))
      )))


