(ns leiningen.clojure-lsp.binary
  (:refer-clojure :exclude [run!])
  (:require
   [babashka.process :as process]
   [clojure.java.io :as io]
   [clojure.string :as string])
  (:import
   [java.io BufferedReader File]
   [java.util.zip ZipInputStream]))

(set! *warn-on-reflection* true)

(defn ^:private global-cache-dir []
  (let [cache-home (or (System/getenv "XDG_CACHE_HOME")
                       (io/file (System/getProperty "user.home") ".cache"))]
    (io/file cache-home "lein-clojure-lsp")))

(def ^:private download-artifact-uri
  "https://github.com/clojure-lsp/clojure-lsp/releases/download/%s/%s")

(defn ^:private os-name []
  (let [os-name (string/lower-case (System/getProperty "os.name" "generic"))]
    (cond
      (string/includes? os-name "win") :windows
      (string/includes? os-name "mac") :macos
      :else :linux)))

(defn ^:private os-arch []
  (if (= "aarch64" (System/getProperty "os.arch"))
    :aarch64
    :amd64))

(def ^:private artifacts
  {:linux {:amd64 "clojure-lsp-native-static-linux-amd64.zip"
           :aarch64 "clojure-lsp-native-linux-aarch64.zip"}
   :macos {:amd64 "clojure-lsp-native-macos-amd64.zip"
           :aarch64 "clojure-lsp-native-macos-aarch64.zip"}
   :windows {:amd64 "clojure-lsp-native-windows-amd64.zip"}})

(defn ^:private unzip-file [input ^File dest-file]
  (with-open [stream (-> input io/input-stream ZipInputStream.)]
    (loop [entry (.getNextEntry stream)]
      (when entry
        (if (.isDirectory entry)
          (when-not (.exists dest-file)
            (.mkdirs dest-file))
          (clojure.java.io/copy stream dest-file))
        (recur (.getNextEntry stream))))))

(defn ^:private download! [^File download-path version]
  (let [platform (os-name)
        arch (os-arch)
        artifact-name (get-in artifacts [platform arch])
        uri (format download-artifact-uri version artifact-name)]
    (io/make-parents download-path)
    (unzip-file (io/input-stream uri) download-path)
    (doto download-path
      (.setWritable true)
      (.setReadable true)
      (.setExecutable true))))

(defn ^:private server-version []
  (string/trim (slurp (io/resource "CLOJURE_LSP_VERSION"))))

(defn ^:private server-path ^File []
  (io/file (global-cache-dir) "clojure-lsp"))

(defn ^:private run-lsp! [^File path args]
  (let [p (process/process {:cmd (concat [(.getAbsolutePath path)] args)})]
    (future
      (with-open [out-rdr ^BufferedReader (io/reader (:out p))]
        (loop []
          (when-let [line (.readLine out-rdr)]
            (println line)
            (recur)))))
    (future
      (with-open [out-rdr ^BufferedReader (io/reader (:err p))]
        (binding [*out* *err*]
          (loop []
            (when-let [line (.readLine out-rdr)]
              (println line)
              (recur))))))
    @p))

(defn run! [args]
  (let [server-path (server-path)
        server-version (server-version)]
    (when-not (.exists server-path)
      (binding [*out* *err*]
        (println "Downloading and caching clojure-lsp to" (str server-path)))
      (let [t (System/currentTimeMillis)]
        (download! server-path server-version)
        (binding [*out* *err*]
          (println (format "Downloaded clojure-lsp took %sms" (- (System/currentTimeMillis) t))))))
    (run-lsp! server-path args)))
