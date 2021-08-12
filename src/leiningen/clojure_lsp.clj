(ns leiningen.clojure-lsp
  (:require [clojure-lsp.api :as lsp]))

(defn clojure-lsp
  "Access clojure-lsp API features."
  [project & args]
  lsp/clean-ns!)
