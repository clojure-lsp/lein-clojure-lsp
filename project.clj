(defproject com.github.clojure-lsp/lein-clojure-lsp "1.4.11"
  :description "Lein plugin to run clojure-lsp features via API."
  :url "https://clojure-lsp.github.io/clojure-lsp"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :eval-in-leiningen true
  :pedantic? :abort
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password
                                    :sign-releases false}]]
  :managed-dependencies [[com.fasterxml.jackson.core/jackson-core "2.15.2"]
                         [org.clojure/tools.cli "1.0.219"]]
  :dependencies [[com.github.clojure-lsp/clojure-lsp-server "2024.08.05-18.16.00"]])
