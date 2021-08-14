# lein-clojure-lsp

A Leiningen plugin to use [clojure-lsp](https://clojure-lsp.github.io/clojure-lsp/) features API.

## Installation

Add the plugin to your `project.clj`:

```clojure
:plugins [[com.github.clojure-lsp/lein-clojure-lsp "0.1.0"]]
```

## Usage

This plugin accepts the following pattern `clojure-lsp <command> "<options-as-edn>"`.

For more information on all available commands and options, Check the [API documentation](https://clojure-lsp.github.io/clojure-lsp/api/).

### lein CLI

``` bash
$ lein clojure-lsp clean-ns
```

### Aliases

You can configure your project.clj to add custom aliases to run specific clojure-lsp tasks, below you can find a simple example with both tasks for formatting, cleaning the namespaces as aliases to just check instead of change the code (dry):

```clojure
,,,
:clojure-lsp {:settings {:clean {:ns-inner-blocks-indentation :same-line}}} ;; API options
:aliases {"clean-ns" ["clojure-lsp" "clean-ns" "{:dry? true}"]   ;; check if namespaces are clean
          "format" ["clojure-lsp" "format" "{:dry? true}"]       ;; check if namespaces are formatted
          "lint" ["do" ["clean-ns"] ["format"]]                  ;; check both

          "clean-ns-fix" ["clojure-lsp" "clean-ns"]              ;; Fix namespaces not clean
          "format-fix" ["clojure-lsp" "format"]                  ;; Fix namespaces not formatted
          "lint-fix" ["do" ["clean-ns-fix"] ["format-fix"]]}     ;; Fix both
,,,
```
