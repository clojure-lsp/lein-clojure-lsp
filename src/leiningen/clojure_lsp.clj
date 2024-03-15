(ns leiningen.clojure-lsp
  (:refer-clojure :exclude [run!])
  (:require
   [clojure.edn :as edn]
   [clojure-lsp.main :as lsp]
   [leiningen.core.main :as lein-core]))

(defn ^:private has-settings?
  [command-and-options]
  (let [settings-index (.indexOf command-and-options "--settings")]
    (and (>= settings-index 0)
         (not-empty (nth command-and-options (inc settings-index))))))

(defn ^:private args-with-merged-settings
  [command-and-options project-settings]
  (let [settings-index (inc (.indexOf command-and-options "--settings"))
        settings (edn/read-string (nth command-and-options settings-index))]
    (assoc (vec command-and-options) settings-index (str (merge project-settings settings)))))

(defn args
  [command-and-options project-settings]
  (cond
    (and (not-empty project-settings) (has-settings? command-and-options))
    (args-with-merged-settings command-and-options project-settings)

    (not-empty project-settings)
    (concat command-and-options ["--settings" (str project-settings)])

    :else
    command-and-options))

(defn ^:private run!
  [command-and-options project-settings]
  (let [result (apply lsp/run! (args command-and-options project-settings))]
    (when-let [message-fn (:message-fn result)]
      (println (message-fn)))
    (when (not= 0 (:result-code result))
      (lein-core/exit (:result-code result) "clojure-lsp found issues"))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn ^:no-project-needed clojure-lsp
  "Access clojure-lsp API features"
  [project & command-and-options]
  (let [project-settings (or (:clojure-lsp project) {})]
    (if lein-core/*info*
      (run! command-and-options project-settings)
      (with-out-str
        (run! command-and-options project-settings)))))
