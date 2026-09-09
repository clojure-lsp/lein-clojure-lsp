(ns leiningen.clojure-lsp.binary-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [leiningen.clojure-lsp.binary :as binary])
  (:import
   [java.io ByteArrayInputStream ByteArrayOutputStream File]
   [java.util.zip ZipEntry ZipOutputStream]))

(defn zipped-bytes [content]
  (let [output (ByteArrayOutputStream.)]
    (with-open [zip-output (ZipOutputStream. output)]
      (.putNextEntry zip-output (ZipEntry. "clojure-lsp"))
      (.write zip-output (.getBytes content "UTF-8"))
      (.closeEntry zip-output))
    (.toByteArray output)))

(defn temp-directory []
  (doto (File/createTempFile "lein-clojure-lsp-" "")
    (.delete)
    (.mkdir)))

(defn delete-directory! [directory]
  (doseq [file (reverse (file-seq directory))]
    (.delete ^File file)))

(deftest install-test
  (let [directory (temp-directory)
        destination (io/file directory "clojure-lsp")]
    (try
      (spit destination "existing-content-that-is-longer")
      (#'binary/install! (ByteArrayInputStream. (zipped-bytes "new-content")) destination)
      (is (= "new-content" (slurp destination)))
      (is (.canExecute destination))
      (finally
        (delete-directory! directory)))))

(deftest server-path-test
  (let [version-a (#'binary/server-path "version-a")
        version-b (#'binary/server-path "version-b")]
    (is (not= version-a version-b))
    (is (= "version-a" (.getName (.getParentFile version-a))))
    (is (= "version-b" (.getName (.getParentFile version-b))))))
