(defproject com.github.clojure-lsp/lein-clojure-lsp "1.3.20"
  :description "Lein plugin to run clojure-lsp features via API."
  :url "https://clojure-lsp.github.io/clojure-lsp"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :eval-in-leiningen true
  :pedantic? :warn
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password
                                    :sign-releases false}]]
  :dependencies [[com.github.clojure-lsp/clojure-lsp-server "2023.02.27-13.12.12"]])
