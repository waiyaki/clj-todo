(ns webdev.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.handler.dump :refer [handle-dump]]
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


(defn about [req]
  {:status  200
   :body    "Hey! Name's James. Check out this really cool app!"
   :headers {}})


(defn yo [req]
  (let [name (get-in req [:route-params :name])]
    {:status  200
     :body    (str "Yo! " name "!")
     :headers {}}))


(def ops
  {"+" +
   "-" -
   "*" *
   ":" /})


(defn calc [req]
  (let [a (Integer. (get-in req [:route-params :a]))
        b (Integer. (get-in req [:route-params :b]))
        op (get-in req [:route-params :op])
        f (get ops op)]
    (if f
      {:status 200
       :body (str (f a b))
       :headers {}}
      {:status 404
       :body (str "Unknown operator: " op)
       :headers {}})))


(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)
  (not-found "Page not found."))


(defn -main
  [port]
  (jetty/run-jetty
    app
    {:port (Integer. port)}))
