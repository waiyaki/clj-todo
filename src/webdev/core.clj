(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))


(defn greet [req]
  {:status  200
   :body    "Hello there!"
   :headers {}})


(defn goodbye [req]
  {:status  200
   :body    "Goodbye, Cruel World!"
   :headers {}})

(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (not-found "Page not found."))


(defn -main
  [port]
  (jetty/run-jetty
    app
    {:port (Integer. port)}))
