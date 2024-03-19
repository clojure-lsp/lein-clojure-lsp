(ns leiningen.clojure-lsp-test
  (:require [clojure.test :refer :all]
            [leiningen.clojure-lsp :as clojure-lsp]))

(deftest args-test
  (testing "Should return the arguments when project settings is an empty map"
    (is (= ["diagnostics"]
           (clojure-lsp/args ["diagnostics"] {}))))

  (testing "Should return the arguments when project settings is nil"
    (is (= ["diagnostics"]
           (clojure-lsp/args ["diagnostics"] nil))))

  (testing "Should return the arguments when there is project settings"
    (is (= ["diagnostics" "--settings" "{:foo 1}"]
           (clojure-lsp/args ["diagnostics"] {:foo 1}))))

  (testing "Should return the arguments when there is settings options and also project settings"
    (is (= ["diagnostics" "--settings" "{:foo 1, :bar \"Test\"}"]
           (clojure-lsp/args ["diagnostics" "--settings" "{:bar \"Test\"}"] {:foo 1}))))

  (testing "Should return the arguments when there is settings options and there is no project settings"
    (is (= ["diagnostics" "--settings" "{:bar \"Test\"}"]
           (clojure-lsp/args ["diagnostics" "--settings" "{:bar \"Test\"}"] {})))))
