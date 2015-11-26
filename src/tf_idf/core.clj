(ns tf-idf.core
  (:require [clojure.string :as string]
            [sparkling.conf :as conf]
            [sparkling.core :as spark]
            [sparkling.serialization]
            [sparkling.destructuring :as s-de])
  (:gen-class))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Basic term handling functions

(def stopwords #{"a" "all" "and" "any" "are" "is" "in" "of" "on"
                 "or" "our" "so" "this" "the" "that" "to" "we"})

(defn terms [content]
  (map string/lower-case (string/split content #" ")))

(def remove-stopwords (partial remove (partial contains? stopwords)))


(remove-stopwords (terms "A quick brown fox jumps"))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; tf / idf / tf*idf functions

(defn idf [doc-count doc-count-for-term]
  (Math/log (/ doc-count (+ 1.0 doc-count-for-term))))


(System/getenv "SPARK_LOCAL_IP")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Basic Spark management

(defn make-spark-context []
  (let [c (-> (conf/spark-conf)
              (conf/master "local[*]")
              (conf/app-name "tfidf"))]
    (spark/spark-context c)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Basic data model generation functions
(defn term-count-from-doc
  "Returns a stopword filtered seq of tuples of doc-id,[term term-count doc-terms-count]"
  [doc-id content]
  (let [terms (remove-stopwords
                (terms content))
        doc-terms-count (count terms)
        term-count (frequencies terms)]
    (map (fn [term] (spark/tuple [doc-id term] [(term-count term) doc-terms-count]))
         (distinct terms))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Spark Transformations / Actions

(defn document-count [documents]
  (spark/count documents))

; (term-count-from-doc "doc1" "A quick brown fox")


(defn term-count-by-doc-term [documents]
  (->>
    documents
    (spark/flat-map-to-pair
      (s-de/key-value-fn term-count-from-doc))
    spark/cache))

(defn document-count-by-term [document-term-count]
  (->> document-term-count
       (spark/map-to-pair (s-de/key-value-fn
                            (fn [[_ term] [_ _]] (spark/tuple term 1))))
       (spark/reduce-by-key +)))

(defn idf-by-term [doc-count doc-count-for-term-rdd]
  (spark/map-values (partial idf doc-count) doc-count-for-term-rdd))

(defn tf-by-doc-term [document-term-count]
  (spark/map-to-pair (s-de/key-value-fn
                       (fn [[doc term] [term-count doc-terms-count]]
                         (spark/tuple term [doc (/ term-count doc-terms-count)])))
                     document-term-count))


(defn tf-idf-by-doc-term [doc-count document-term-count term-idf]
  (->> (spark/join (tf-by-doc-term document-term-count) term-idf)
       (spark/map-to-pair (s-de/key-val-val-fn
                            (fn [term [doc tf] idf]
                              (spark/tuple [doc term] (* tf idf)))))
       ))


(defn tf-idf [corpus]
  (let [doc-count (document-count corpus)
        document-term-count (term-count-by-doc-term corpus)
        term-idf (idf-by-term doc-count (document-count-by-term document-term-count))]
    (tf-idf-by-doc-term doc-count document-term-count term-idf)))

(def tuple-swap (memfn ^scala.Tuple2 swap))

(def swap-key-value (partial spark/map-to-pair tuple-swap))

(defn sort-by-value [rdd]
  (->> rdd
       swap-key-value
       (spark/sort-by-key compare false)
       swap-key-value
       ))



(defn -main [& args]
  (let [sc (make-spark-context)
        documents [(spark/tuple :doc1 "Four score and seven years ago our fathers brought forth on this continent a new nation")
                   (spark/tuple :doc2 "conceived in Liberty and dedicated to the proposition that all men are created equal")
                   (spark/tuple :doc3 "Now we are engaged in a great civil war testing whether that nation or any nation so")
                   (spark/tuple :doc4 "conceived and so dedicated can long endure We are met on a great battlefield of that war")]
        corpus (spark/parallelize-pairs sc documents)
        tf-idf (tf-idf corpus)]
    (println (.toDebugString tf-idf))
    (clojure.pprint/pprint (spark/collect tf-idf))
    #_(clojure.pprint/pprint (spark/take 10 (sort-by-value tf-idf)))
    ))
