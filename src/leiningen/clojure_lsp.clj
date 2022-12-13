(ns leiningen.clojure-lsp
  (:refer-clojure :exclude [run!])
  (:require
   [clojure-lsp.main :as lsp]
   [leiningen.core.main :as lein-core]))

(defn run! [command-and-options options]
  (let [result (apply lsp/run! (concat command-and-options ["--settings" (str options)]))]
    (when-let [message-fn (:message-fn result)]
      (println (message-fn)))
    (when (not= 0 (:result-code result))
      (lein-core/exit (:result-code result) "clojure-lsp found issues"))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn ^:no-project-needed clojure-lsp
  "Access clojure-lsp API features"
  [project & command-and-options]
  (let [project-options (or (:clojure-lsp project) {})]
    (if lein-core/*info*
      (run! command-and-options project-options)
      (with-out-str
        (run! command-and-options project-options)))))
