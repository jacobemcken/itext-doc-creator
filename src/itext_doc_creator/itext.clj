(ns itext-doc-creator.itext
  "For HTML to PDF conversion using iText and for adding and extracting metadata with iText."
  (:import (com.itextpdf.html2pdf ConverterProperties HtmlConverter)
           (com.itextpdf.html2pdf.attach.impl DefaultTagWorkerFactory OutlineHandler)
           (com.itextpdf.html2pdf.attach.impl.tags ATagWorker SvgTagWorker)
           (com.itextpdf.html2pdf.html TagConstants)
           (com.itextpdf.kernel.pdf WriterProperties PdfDocument PdfViewerPreferences PdfWriter)
           (java.io ByteArrayOutputStream OutputStream)))

(def tag-worker-factory
  (proxy [DefaultTagWorkerFactory] []
    (getCustomTagWorker [tag context]
      (condp #(contains? %1 %2) (.name tag)
        #{TagConstants/SVG} (proxy [SvgTagWorker] [tag context]
                              (getElementResult []
                                (let [element-result (proxy-super getElementResult)
                                      acc-prop (.getAccessibilityProperties element-result)]
                                  (if (= "true" (.getAttribute tag "aria-hidden"))
                                    (.setRole acc-prop nil)
                                    (.setAlternateDescription acc-prop (not-empty (.getAttribute tag "aria-label"))))
                                  element-result)))
        #{TagConstants/A} (proxy [ATagWorker] [tag context]
                            (getElementResult []
                              (doseq [element (.getAllElements this)]
                                (.setAlternateDescription
                                 (.getAccessibilityProperties element)
                                 (not-empty (.getAttribute tag "title"))))
                              (proxy-super getElementResult)))
        nil))))

(defn get-pdf-document
  "Creates and returns a PdfDocument from an output stream.
   The document is marked as being tagged, use document title as window title,
   XMP Metadata is added and FullCompressionMode is turned on."
  ^PdfDocument [^OutputStream output-stream]
  (let [writer (PdfWriter. output-stream
                           (doto (WriterProperties.)
                             (.addUAXmpMetadata)
                             (.setFullCompressionMode true)))
        document (PdfDocument. writer)]
    (-> (.getCatalog document)
        (.setViewerPreferences (.setDisplayDocTitle (PdfViewerPreferences.) true)))
    (.setTagged document)
    document))

(defn html->pdf
  "Converts an HTML String to a PDF."
  ^ByteArrayOutputStream [^String html ^ByteArrayOutputStream output-stream]
  (with-open [document (get-pdf-document output-stream)]
    (let [converter-properties (doto (ConverterProperties.)
                                 (.setOutlineHandler (doto (OutlineHandler/createStandardHandler)
                                                       (.setDestinationNamePrefix (str (random-uuid)))))
                                 (.setTagWorkerFactory tag-worker-factory))]
      (HtmlConverter/convertToPdf html document converter-properties)
      output-stream)))
