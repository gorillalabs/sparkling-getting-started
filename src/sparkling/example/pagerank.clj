(ns sparkling.example.pagerank
  (:require [sparkling.api :as spark]
            [sparkling.destructuring :as s-de]
            [sparkling.debug :as s-dbg]
            [sparkling.conf :as conf])
  (:gen-class))

(def master "local")
(def conf {})
(def env {})

(defn contribs [links ranks]
  (println "A step of contributions:\n  " (spark/collect links) "\n\n  " (spark/collect ranks))
  (let [contribs
        (-> links
            (spark/join ranks)
            (spark/values)
            (spark/flat-map-to-pair (s-de/val-val-fn (fn [urls, rank]
                                                       (println (seq urls) ", " rank)
                                                       (let [urls (seq urls)
                                                             size (count urls)]
                                                         (map (fn [url] (spark/tuple url, (/ rank size))) urls))))))]

    (-> (spark/reduce-by-key contribs +)
        (spark/map-values (fn [x] (+ 0.15 (* 0.85 x)))))))

(defn pagerank-step [iterations-to-go links ranks]
  (if (zero? iterations-to-go)
    ranks
    (recur (dec iterations-to-go) links (contribs links ranks))))


(defn -main [& args]
  (try
    (let [c (-> (conf/spark-conf)
                (conf/master master)
                (conf/app-name "pagerank")
                (conf/set "spark.akka.timeout" "300")
                (conf/set conf)
                (conf/set-executor-env env))
          sc (spark/spark-context c)

          ;; sample urls and links
          pages [(spark/tuple "url1" "url2")
                 (spark/tuple "url2" "url4")
                 (spark/tuple "url3" "url2")
                 (spark/tuple "url4" "url3")]

          links (-> (spark/parallelize-pairs sc pages)
                    (spark/group-by-key)
                    (spark/cache))
          ;          _ (s-dbg/inspect links "links")

          ranks (spark/map-values links (fn [_] 1.0))
          ;          _ (s-dbg/inspect ranks "ranks")

          ]



      (->
        (pagerank-step 100 links ranks)
        spark/collect
        clojure.pprint/pprint))
    (catch Exception e
      (println (.printStackTrace e)))))
