(ns webdev.core
  (:gen-class)
  (:require [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [ring.handler.dump :refer [handle-dump]]
            [webdev.item.model :as items]))


(def db "jdbc:postgresql://localhost/webdev")


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


(defroutes routes
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:a/:op/:b" [] calc)

  (GET "/about" [] about)
  (GET "/request" [] handle-dump)
  (not-found "Page not found."))


(def app
  (wrap-params
    routes))


(defn -main
  [port]
  (items/create-table! db)
  (jetty/run-jetty app {:port (Integer. port)}))
