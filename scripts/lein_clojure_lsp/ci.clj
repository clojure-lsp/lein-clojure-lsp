(ns lein-clojure-lsp.ci
  (:require
   [babashka.tasks :refer [shell]]
   [clojure.string :as string]))

(defn ^:private replace-in-file [file regex content]
  (as-> (slurp file) $
    (string/replace $ regex content)
    (spit file $)))

(defn ^:private add-changelog-entry [tag comment]
  (replace-in-file "CHANGELOG.md"
                   #"## Unreleased"
                   (if comment
                     (format "## Unreleased\n\n## %s\n\n- %s" tag comment)
                     (format "## Unreleased\n\n## %s" tag))))

(defn ^:private replace-tag [tag]
  (replace-in-file "project.clj"
                   #"com.github.clojure-lsp/lein-clojure-lsp \"[0-9]+.[0-9]+.[0-9]+.*\""
                   (format "com.github.clojure-lsp/lein-clojure-lsp \"%s\"" tag))
  (replace-in-file "README.md"
                   #"com.github.clojure-lsp/lein-clojure-lsp \"[0-9]+.[0-9]+.[0-9]+.*\""
                   (format "com.github.clojure-lsp/lein-clojure-lsp \"%s\"" tag)))

(defn ^:private get-patched-tag []
  (let [[major minor patch]
        (-> (re-find #"com.github.clojure-lsp/lein-clojure-lsp \"(.*)\"" (slurp "project.clj"))
            last
            (string/split #"\."))]
    (str major "." minor "." (inc (Integer/parseInt patch)))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn tag [& [tag]]
  (shell "git fetch origin")
  (shell "git pull origin HEAD")
  (replace-tag tag)
  (add-changelog-entry tag nil)
  (shell "git add project.clj README.md CHANGELOG.md")
  (shell (format "git commit -m \"Release: %s\"" tag))
  (shell (str "git tag " tag))
  (shell "git push origin HEAD")
  (shell "git push origin --tags"))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn tag-patch-for-version [& [version]]
  (shell "git fetch origin")
  (shell "git pull origin HEAD")
  (spit "resources/CLOJURE_LSP_VERSION" version)
  (let [new-tag (get-patched-tag)]
    (replace-tag new-tag)
    (add-changelog-entry new-tag (str "Bump clojure-lsp to " version))
    (shell "git add project.clj README.md CHANGELOG.md resources/CLOJURE_LSP_VERSION")
    (shell (format "git commit -m \"Release: %s\"" new-tag))
    (shell (str "git tag " new-tag)))
  (shell "git push origin HEAD")
  (shell "git push origin --tags"))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn deploy [& _args]
  (shell "lein deploy clojars"))
