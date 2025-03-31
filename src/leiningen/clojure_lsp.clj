(ns leiningen.clojure-lsp
  (:refer-clojure :exclude [run!])
  (:require
   [clojure.edn :as edn]
   [leiningen.core.main :as lein-core]
   [leiningen.clojure-lsp.binary :as lsp-binary]))

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
    (and (not-empty project-settings)
         (has-settings? command-and-options))
    (args-with-merged-settings command-and-options project-settings)

    (not-empty project-settings)
    (concat command-and-options ["--settings" (str (:settings project-settings))])

    :else
    command-and-options))

(defn ^:private run!
  [command-and-options project-settings]
  (let [{:keys [exit]} (lsp-binary/run! (args command-and-options project-settings))]
    (when (not= 0 exit)
      (lein-core/exit exit "clojure-lsp found issues"))))

(defn ^:no-project-needed clojure-lsp
  "Access clojure-lsp API features"
  [project & command-and-options]
  (let [project-settings (or (:clojure-lsp project) {})]
    (if lein-core/*info*
      (run! command-and-options project-settings)
      (with-out-str
        (run! command-and-options project-settings)))))
