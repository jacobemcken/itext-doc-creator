(ns itext-doc-creator.core
  (:require [itext-doc-creator.itext :as itext]
            [mount.core :as mount]
            [org.httpkit.server :as httpkit]
            [reitit.ring :as ring]
            [ring.util.io :as ring-io])
  (:gen-class))

(defn handler
  []
  (ring/ring-handler
   (ring/router
    [["/health" (fn [_req] {:status 200})]
     ["/html2pdf" (fn [req]
                    (if-let [html (some-> req :body slurp)]
                      {:body (ring-io/piped-input-stream (fn [os] (itext/html->pdf html os)))}
                      {:status 400 :body "Body must contain data"}))]])
   (ring/create-default-handler)))

(defn start
  [{:keys [port] :as _conf}]
  (println (str "Starting on port " port "."))
  (httpkit/run-server (handler) {:port port}))

(defn stop
  [server]
  ;; graceful shutdown: wait 100ms for existing requests to be finished
  ;; :timeout is optional, when no timeout, stop immediately
  (server :timeout 100))

(mount/defstate http-server
  :start (start {:port (or (some-> (System/getenv "PORT") Integer/parseInt) 9000)})
  :stop (stop http-server))

(defn -main
  [& _args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. ^Runnable #(do (println "Shutting down.")
                                            (mount/stop))))
  (mount/start))

(comment
  
  (mount/start)
  (mount/stop)
  )
