(ns webdev.item.handler
  (:require [webdev.item.model :as model]
            [webdev.item.view :as view]))


(defn handle-index-items [req]
  (let [db    (:webdev/db req)
        items (model/read-items db)]
    {:status  200
     :headers {}
     :body (view/items-page items)}))


(defn handle-create-item [req]
  (let [{:keys                      [webdev/db]
         {:strs [name description]} :params} req
        item-id
        (model/create-item db name description)]
    {:status  302
     :headers {"Location" "/items"}
     :body    ""}))


(defn handle-delete-item [req]
  (let [db      (:webdev/db req)
        item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        exists? (model/delete-item db item-id)]
    (if exists?
      {:status  302
       :headers {"Location" "/items"}
       :body    ""}
      {:status  404
       :body    "Item not found"
       :headers {}})))


(defn handle-update-item [req]
  (let [db      (:webdev/db req)
        item-id (java.util.UUID/fromString (:item-id (:route-params req)))
        checked (get-in req [:params "checked"])
        exists? (model/update-item db item-id (= "true" checked))]
    (if exists?
      {:status  302
       :headers {"Location" "/items"}
       :body    ""}
      {:status  404
       :body    "Item not found."
       :headers {}})))
