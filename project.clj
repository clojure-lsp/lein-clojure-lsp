(defproject com.github.clojure-lsp/lein-clojure-lsp "2.0.14"
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
  :dependencies [[babashka/process "0.6.23"]])
