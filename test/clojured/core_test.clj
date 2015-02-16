(ns clojured.core-test
  (:require [clojured.core :refer :all]
            [clojure.test :refer :all]
            [sparkling.conf :as conf]
            [sparkling.core :as spark]
            [clj-time.format :as tf]))

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



(deftest test-parse-line
  (let [testline "87.161.251.240 - - [22/Jun/2014:02:20:03 +0200] \"GET /blubb?key=value HTTP/1.0\" 200 13751 \"http://blublii.net/\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36\""]
    (testing
      "no lines return 0"
      (is (= {:duration  ""
              :ip        "87.161.251.240"
              :length    "13751"
              :referer   "http://blublii.net/"
              :request   "GET /blubb?key=value HTTP/1.0"
              :status    "200"
              :timestamp (tf/parse (:date-time tf/formatters) "2014-06-22T00:20:03.000Z")
              :ua        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36"
              :uri       "/blubb"}
             (parse-line testline))))
    ))
