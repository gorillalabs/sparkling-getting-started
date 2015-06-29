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
            [sparkling.api :as spark]
            [sparkling.destructuring :as s-de]
            [clojure.string :as s]))

(def c (-> (conf/spark-conf)
           (conf/master "local")
           (conf/app-name "sparkling-example")))

(def sc (spark/spark-context c))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;flow/sc</span>","value":"#'flow/sc"}
;; <=

;; **
;;; ## Define some RDDs
;; **

;; @@
(def data (spark/parallelize-pairs sc [ (spark/tuple "a" 1) (spark/tuple "b" 2) (spark/tuple "c" 3) (spark/tuple "d" 4) (spark/tuple "e" 5)]))

(spark/first data)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#sparkling/tuple [&quot;a&quot; 1]</span>","value":"#sparkling/tuple [\"a\" 1]"}
;; <=

;; **
;;; ## Do something with the RDDs
;; **

;; @@
(plot/list-plot
	(-> (spark/parallelize sc [1 2 3 4 5])
    	(spark/map (fn [x] (* x x)))
    	spark/collect
    	))
;; @@
;; =>
;;; {"type":"vega","content":{"axes":[{"scale":"x","type":"x"},{"scale":"y","type":"y"}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"404c1629-f09c-4016-b69d-c27e4748568a","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"404c1629-f09c-4016-b69d-c27e4748568a","field":"data.y"}}],"marks":[{"type":"symbol","from":{"data":"404c1629-f09c-4016-b69d-c27e4748568a"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"steelblue"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}}],"data":[{"name":"404c1629-f09c-4016-b69d-c27e4748568a","values":[{"x":0,"y":1},{"x":1,"y":4},{"x":2,"y":9},{"x":3,"y":16},{"x":4,"y":25}]}],"width":400,"height":247.2187957763672,"padding":{"bottom":20,"top":10,"right":10,"left":50}},"value":"#gorilla_repl.vega.VegaView{:content {:axes [{:scale \"x\", :type \"x\"} {:scale \"y\", :type \"y\"}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"404c1629-f09c-4016-b69d-c27e4748568a\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"404c1629-f09c-4016-b69d-c27e4748568a\", :field \"data.y\"}}], :marks [{:type \"symbol\", :from {:data \"404c1629-f09c-4016-b69d-c27e4748568a\"}, :properties {:enter {:x {:scale \"x\", :field \"data.x\"}, :y {:scale \"y\", :field \"data.y\"}, :fill {:value \"steelblue\"}, :fillOpacity {:value 1}}, :update {:shape \"circle\", :size {:value 70}, :stroke {:value \"transparent\"}}, :hover {:size {:value 210}, :stroke {:value \"white\"}}}}], :data [{:name \"404c1629-f09c-4016-b69d-c27e4748568a\", :values ({:x 0, :y 1} {:x 1, :y 4} {:x 2, :y 9} {:x 3, :y 16} {:x 4, :y 25})}], :width 400, :height 247.2188, :padding {:bottom 20, :top 10, :right 10, :left 50}}}"}
;; <=

;; @@
(require '[sparkling.example.tfidf :as tfidf :reload true])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(tfidf/-main)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc2&quot;</span>","value":"\"doc2\""},{"type":"html","content":"<span class='clj-string'>&quot;created&quot;</span>","value":"\"created\""},{"type":"html","content":"<span class='clj-double'>0.09902102579427793</span>","value":"0.09902102579427793"}],"value":"[\"doc2\" \"created\" 0.09902102579427793]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc2&quot;</span>","value":"\"doc2\""},{"type":"html","content":"<span class='clj-string'>&quot;men&quot;</span>","value":"\"men\""},{"type":"html","content":"<span class='clj-double'>0.09902102579427793</span>","value":"0.09902102579427793"}],"value":"[\"doc2\" \"men\" 0.09902102579427793]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc2&quot;</span>","value":"\"doc2\""},{"type":"html","content":"<span class='clj-string'>&quot;Liberty&quot;</span>","value":"\"Liberty\""},{"type":"html","content":"<span class='clj-double'>0.09902102579427793</span>","value":"0.09902102579427793"}],"value":"[\"doc2\" \"Liberty\" 0.09902102579427793]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc2&quot;</span>","value":"\"doc2\""},{"type":"html","content":"<span class='clj-string'>&quot;proposition&quot;</span>","value":"\"proposition\""},{"type":"html","content":"<span class='clj-double'>0.09902102579427793</span>","value":"0.09902102579427793"}],"value":"[\"doc2\" \"proposition\" 0.09902102579427793]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc2&quot;</span>","value":"\"doc2\""},{"type":"html","content":"<span class='clj-string'>&quot;equal&quot;</span>","value":"\"equal\""},{"type":"html","content":"<span class='clj-double'>0.09902102579427793</span>","value":"0.09902102579427793"}],"value":"[\"doc2\" \"equal\" 0.09902102579427793]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc3&quot;</span>","value":"\"doc3\""},{"type":"html","content":"<span class='clj-string'>&quot;civil&quot;</span>","value":"\"civil\""},{"type":"html","content":"<span class='clj-double'>0.07701635339554948</span>","value":"0.07701635339554948"}],"value":"[\"doc3\" \"civil\" 0.07701635339554948]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc3&quot;</span>","value":"\"doc3\""},{"type":"html","content":"<span class='clj-string'>&quot;Now&quot;</span>","value":"\"Now\""},{"type":"html","content":"<span class='clj-double'>0.07701635339554948</span>","value":"0.07701635339554948"}],"value":"[\"doc3\" \"Now\" 0.07701635339554948]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc3&quot;</span>","value":"\"doc3\""},{"type":"html","content":"<span class='clj-string'>&quot;testing&quot;</span>","value":"\"testing\""},{"type":"html","content":"<span class='clj-double'>0.07701635339554948</span>","value":"0.07701635339554948"}],"value":"[\"doc3\" \"testing\" 0.07701635339554948]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc3&quot;</span>","value":"\"doc3\""},{"type":"html","content":"<span class='clj-string'>&quot;engaged&quot;</span>","value":"\"engaged\""},{"type":"html","content":"<span class='clj-double'>0.07701635339554948</span>","value":"0.07701635339554948"}],"value":"[\"doc3\" \"engaged\" 0.07701635339554948]"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;doc3&quot;</span>","value":"\"doc3\""},{"type":"html","content":"<span class='clj-string'>&quot;whether&quot;</span>","value":"\"whether\""},{"type":"html","content":"<span class='clj-double'>0.07701635339554948</span>","value":"0.07701635339554948"}],"value":"[\"doc3\" \"whether\" 0.07701635339554948]"}],"value":"([\"doc2\" \"created\" 0.09902102579427793] [\"doc2\" \"men\" 0.09902102579427793] [\"doc2\" \"Liberty\" 0.09902102579427793] [\"doc2\" \"proposition\" 0.09902102579427793] [\"doc2\" \"equal\" 0.09902102579427793] [\"doc3\" \"civil\" 0.07701635339554948] [\"doc3\" \"Now\" 0.07701635339554948] [\"doc3\" \"testing\" 0.07701635339554948] [\"doc3\" \"engaged\" 0.07701635339554948] [\"doc3\" \"whether\" 0.07701635339554948])"}
;; <=

;; @@

;; @@
