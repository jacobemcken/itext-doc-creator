(defproject itext-doc-creator "0.1.0-SNAPSHOT"
  :description "A generic HTTP service that converts HTML to PDF."
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.itextpdf/html2pdf "4.0.5"]
                 [com.itextpdf/itext7-core "7.2.5" :extension "pom"]
                 [http-kit "2.8.0"]
                 [mount/mount "0.1.21"]
                 [metosin/reitit-ring "0.7.2"]]
  :main itext-doc-creator.core
  :repl-options {:init-ns itext-doc-creator.core})
