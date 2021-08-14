(ns leiningen.clojure-lsp
  (:require
   [clojure-lsp.api :as lsp]
   [clojure.edn :as edn]
   [leiningen.core.main :as lein-core]))

(defn handle-command [command options]
  (let [result (case command
                 "clean-ns" (lsp/clean-ns! options)
                 "format" (lsp/format! options)
                 "rename" (lsp/rename! options)
                 (lein-core/abort "Unknown clojure-lsp command:" command))]
    (when (:result-code result)
      (System/exit (:result-code result)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clojure-lsp
  "Access clojure-lsp API features"
  [project command & [options]]
  (let [options (merge {}
                       (:clojure-lsp project)
                       (and options
                            (edn/read-string options)))]
    (if lein-core/*info*
      (handle-command command options)
      (with-out-str
        (handle-command command options)))))
