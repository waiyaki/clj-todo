(ns webdev.item.view
  (:require [hiccup.page :refer [html5]]
            [hiccup.core :refer [html h]]))



(defn common [& body]
  (html5 {:lang :en}
    [:head
     [:title "Listronica"]
     [:meta {:name    :viewport
             :content "width=device-width, initial-scale=1.0"}]
     [:link {:href "/bootstrap/css/bootstrap.min.css"
             :rel  :stylesheet}]]
    [:body
     [:div.container body]
     [:script {:src "https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"}]
     [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))


(defn new-item []
  (html
    [:form.form-horizontal {:method "POST" :action "/items"}
     [:div.form-group
      [:label.control-label.col-sm-2 {:for :name-input} "Name"]
      [:div.col-sm-10
       [:input#name-input.form-control {:name :name
                                        :placeholder "Name"}]]]
     [:div.form-group
      [:label.control-label.col-sm-2 {:for :desc-input} "Description"]
      [:div.col-sm-10
       [:input#desc-input.form-control {:name :description
                                        :placeholder "Description"}]]]
     [:div.form-group
      [:div.col-sm-offset-2.col-sm-10
       [:button.btn.btn-primary {:type :submit} "New item"]]]]))


(defn delete-item-form [id]
  (html
    [:form {:method "POST" :action (str "/items/" id)}
     [:input {:type :hidden
              :name "_method"
              :value "DELETE"}]
     [:div.btn-group
      [:button.btn.btn-danger.btn-xs {:type :submit} "Delete"]]]))


(defn update-item-form [id checked]
  (html
    [:form {:method "POST" :action (str "/items/" id)}
     [:input {:type :hidden
              :name "_method"
              :value "PUT"}]
     [:input {:type :hidden
              :name "checked"
              :value (if checked "false" "true")}]
     [:div.btn-group
      [:button.btn.btn-primary.btn-xs {:type :submit}
       (if checked "DONE" "TODO")]]]))


(defn items-page [items]
  (common
    [:div.row
     [:h1 "My Items"]
     (if (seq items)
       [:table.table.table-striped
        [:thead
         [:tr
          [:th "Name"]
          [:th "Description"]]]
        [:tbody
         (for [i items]
           [:tr
            [:td (delete-item-form (:id i))]
            [:td (update-item-form (:id i) (:checked i))]
            [:td (h (:name i))]
            [:td (h (:description i))]])]]
       [:div.col-sm-offset-1 "There are no items."])
     [:div.col-sm-6
      [:h2 "Create a new item"]
      (new-item)]]))
