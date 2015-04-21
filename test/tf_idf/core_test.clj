(ns tf-idf.core-test
  (:import [org.apache.spark.serializer KryoSerializer]
           [org.apache.spark.serializer KryoSerializerInstance])
  (:require [clojure.test :refer :all]
            [tf-idf.core :refer :all]
            [sparkling.core :as spark]
            [sparkling.conf :as conf]
            [sparkling.destructuring :as s-de]
            [sparkling.serialization :as requirered-to-have-serializer-class-ready]))


(deftest sparkling-serialization
  (testing "registrator"
    (is (instance? KryoSerializerInstance
                   (.newInstance (KryoSerializer. (-> (conf/spark-conf)
                                                      (conf/master "local")
                                                      )))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helper functions

(def tuple2vec (s-de/key-value-fn vector))

(defn first2vec [rdd]
  (tuple2vec (spark/first rdd)))

(defn all2vec [rdd]
  (map tuple2vec (spark/collect rdd)))

(defn round
  "Round a number to the given precision (number of significant digits)"
  [precision d]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(deftest domain-functions-test
  (testing "tf functions"
    (is (= ["quick" "brown" "fox" "jumps"]
           (remove-stopwords (terms "A quick brown fox jumps"))
           )))

  (testing "idf functions"
    (is (= -0.8109302162163288 (idf 4 8)))
    (is (= -0.2231435513142097 (idf 4 4)))
    (is (= 1.3862943611198906 (idf 4 0))))

  (testing "domain model manipulation functions"
    (is (= [(spark/tuple ["doc1" "four"] [1 (int 3)])
            (spark/tuple ["doc1" "score"] [1 (int 3)])
            (spark/tuple ["doc1" "seven"] [1 (int 3)])]
           (into [] (term-count-from-doc "doc1" "Four score and seven"))))))




(defn make-test-context []
  (-> (conf/spark-conf)
      (conf/master "local")
      (conf/app-name "tfidf-test")))

(def documents-fixture
  [(spark/tuple "doc1" "Four score and seven years ago our fathers brought forth on this continent a new nation")
   (spark/tuple "doc2" "conceived in Liberty and dedicated to the proposition that all men are created equal")
   (spark/tuple "doc3" "Now we are engaged in a great civil war testing whether that nation or any nation so")
   (spark/tuple "doc4" "conceived and so dedicated can long endure We are met on a great battlefield of that war")])



(deftest spark-functions-test
  (spark/with-context c (make-test-context)
                      (testing "count the number of documents"
                        (is (= 4
                               (document-count
                                 (spark/parallelize-pairs c documents-fixture))))
                        (is (= 0
                               (document-count
                                 (spark/parallelize-pairs c []))))
                        )

                      (testing "term-count-by-doc-term"
                        (is (= [["doc1" "four"] [1 11]]
                               (first2vec
                                 (term-count-by-doc-term
                                   (spark/parallelize-pairs c documents-fixture))))))

                      (testing "document-count-by-term"
                        (is (= #{["four" 2] ["eggs" 1]}
                               (into #{} (all2vec
                                           (document-count-by-term
                                             (spark/parallelize-pairs c [#sparkling/tuple [["doc1" "four"] [1 2]]
                                                                         #sparkling/tuple [["doc1" "eggs"] [1 2]]
                                                                         #sparkling/tuple [["doc2" "four"] [1 1]]
                                                                         ])))))))

                      (testing ""
                        (is (= #{["four" ["doc1" 1/2]]
                                 ["four" ["doc2" 1]]
                                 ["eggs" ["doc1" 1/2]]}
                               (into #{} (all2vec
                                           (tf-by-doc-term (spark/parallelize-pairs c [#sparkling/tuple [["doc1" "four"] [1 2]]
                                                                                       #sparkling/tuple [["doc1" "eggs"] [1 2]]
                                                                                       #sparkling/tuple [["doc2" "four"] [1 1]]
                                                                                       ])))))))

                      (testing "idf"
                        (is (= #{["four" (round 4 (Math/log (/ 2 3)))] ["eggs" (round 4 (Math/log (/ 2 2)))]}
                               (into #{}
                                     (map (fn [[term idf]] [term (round 4 idf)])
                                          (all2vec
                                            (idf-by-term 2
                                                         (spark/parallelize-pairs c [#sparkling/tuple ["four" 2]
                                                                                     #sparkling/tuple ["eggs" 1]]
                                                                                  ))))))))

                      ))

