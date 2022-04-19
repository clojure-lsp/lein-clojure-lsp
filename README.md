[![Clojars Project](https://img.shields.io/clojars/v/com.github.clojure-lsp/lein-clojure-lsp.svg)](https://clojars.org/com.github.clojure-lsp/lein-clojure-lsp)

# lein-clojure-lsp

A Leiningen plugin to use [clojure-lsp](https://clojure-lsp.io/) features API.

## Installation

Add the plugin to your `project.clj`:

```clojure
:plugins [[com.github.clojure-lsp/lein-clojure-lsp "1.3.1"]]
```

## Usage

This plugin accepts the following pattern `clojure-lsp <command> "<options>"`.

For more information on all available commands and options, Check the [API documentation](https://clojure-lsp.io/api/).

### lein CLI

``` bash
$ lein clojure-lsp clean-ns --dry
```

### Aliases

You can configure your project.clj to add custom aliases to run specific clojure-lsp tasks, below you can find a simple example with both tasks for formatting, cleaning the namespaces as aliases to just check instead of change the code (dry):

```clojure
,,,
:clojure-lsp {:settings {:clean {:ns-inner-blocks-indentation :same-line}}} ;; API options
:aliases {"clean-ns" ["clojure-lsp" "clean-ns" "--dry"]          ;; check if namespaces are clean
          "format" ["clojure-lsp" "format" "--dry"]              ;; check if namespaces are formatted
          "lint" ["do" ["clean-ns"] ["format"]]                  ;; check both

          "clean-ns-fix" ["clojure-lsp" "clean-ns"]              ;; Fix namespaces not clean
          "format-fix" ["clojure-lsp" "format"]                  ;; Fix namespaces not formatted
          "lint-fix" ["do" ["clean-ns-fix"] ["format-fix"]]}     ;; Fix both
,,,
```
