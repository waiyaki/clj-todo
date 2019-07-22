(ns webdev.core
  (:gen-class)
  (:require [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.handler.dump :refer [handle-dump]]
            [webdev.item.model :as items]
            [webdev.item.handler :as handler]))


(def db (or (System/getenv "DATABASE_URL")
          "jdbc:postgresql://localhost/webdev"))


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
  (ANY "/request" [] handle-dump)

  (GET "/items" [] handler/handle-index-items)
  (POST "/items" [] handler/handle-create-item)
  (PUT "/items/:item-id" [] handler/handle-update-item)
  (DELETE "/items/:item-id" [] handler/handle-delete-item)

  (not-found "Page not found."))


(defn wrap-db [handler]
  (fn [req]
    (handler (assoc req :webdev/db db))))


(defn wrap-server [handler]
  (fn [req]
    (assoc-in (handler req) [:headers "Server"] "Listronica 9000")))


(def sim-methods {"PUT"    :put
                  "DELETE" :delete})


(defn wrap-simulated-methods [handler]
  (fn [req]
    (if-let [method (and (= :post (:request-method req))
                      (sim-methods (get-in req [:params "_method"])))]
      (handler (assoc req :request-method method))
      (handler req))))


(def app
  (wrap-server
    (wrap-file-info
      (wrap-resource
        (wrap-db
          (wrap-params
            (wrap-simulated-methods
              routes)))
        "static"))))


(defn -main
  [port]
  (items/create-table! db)
  (jetty/run-jetty app {:port (Integer. port)}))
