(ns leiningen.clojure-lsp
  (:require
   [clojure-lsp.api :as lsp]
   [leiningen.core.main :as lein-core]))

(defn handle-command [command options]
  (case command
    "clean-ns" (lsp/clean-ns! options)
    (lein-core/abort "Unknown clojure-lsp command:" command)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn ^:no-project-needed clojure-lsp
  "Access clojure-lsp API features."
  [project command & _args]
  (let [options (or (:clojure-lsp project) {})]
    (lein-core/info options)
    (if lein-core/*info*
      (handle-command command options)
      (with-out-str
        (handle-command command options)))))
