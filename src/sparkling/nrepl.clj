(ns sparkling.nrepl
  (:require [clojure.tools.nrepl.server :as nrepl-server]
            [gorilla-repl.render-values-mw :as render-mw] ;; it's essential this import comes after the previous one! It
                                                          ;; refers directly to a var in nrepl (as a hack to workaround
                                                          ;; a weakness in nREPL's middleware resolution).
            [cider.nrepl :as cider]
            [gorilla-repl.renderer]
            ))

(def handler
  (let [cider-mw (map resolve cider/cider-middleware)
        middleware (conj cider-mw #'render-mw/render-values)
        ]
    (apply nrepl-server/default-handler middleware)))
