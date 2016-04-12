;; gorilla-repl.fileformat = 1

;; **
;;; # Welcome to Gorillalabs/Sparkling Workbench
;;; 
;;; Shift + enter evaluates code. Hit ctrl+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;; **

;; **
;;; ## Initialize a Namespace
;; **

;; @@
(ns flow
  (:require [gorilla-plot.core :as plot]
            [sparkling.conf :as conf]
            [sparkling.core :as spark]
            [sparkling.destructuring :as s-de]
            [clojure.string :as s]))

(def c (-> (conf/spark-conf)
           (conf/master "local")
           (conf/app-name "sparkling-example")))

(def sc (spark/spark-context c))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flow/c</span>","value":"#'flow/c"}
;; <=

;; **
;;; ## Define some RDDs
;; **

;; @@
(def data (spark/parallelize-pairs sc [ (spark/tuple "a" 1) (spark/tuple "b" 2) (spark/tuple "c" 3) (spark/tuple "d" 4) (spark/tuple "e" 5)]))

(spark/first data)
;; @@

;; **
;;; ## Do something with the RDDs
;; **

;; @@
(plot/list-plot
	(->> (spark/parallelize sc [1 2 3 4 5])
    	(spark/map (fn [x] (* x x)))
    	spark/collect
    	))
;; @@

;; @@
(spark/stop sc)
(require '[sparkling.example.tfidf :as tfidf :reload true])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(tfidf/-main)
;; @@
;; ->
;;; nil
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@

;; @@
