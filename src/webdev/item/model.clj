(ns webdev.item.model
  (:require [clojure.java.jdbc :as db]))


(defn create-table! [db]
  (db/execute!
    db
    ["CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\""])
  (db/execute!
    db
    ["CREATE TABLE IF NOT EXISTS items
       (id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        name TEXT NOT NULL,
        description TEXT NOT NULL,
        checked BOOLEAN NOT NULL DEFAULT FALSE,
        date_created TIMESTAMPTZ NOT NULL DEFAULT now())"]))


(defn create-item [db name desc]
  (:id (first (db/query
                db
                ["INSERT INTO items (name, description)
                  VALUES (?, ?)
                  RETURNING id"
                 name
                 desc]))))


(defn update-item [db id checked]
  (= [1] (db/execute!
           db
           ["UPDATE items
             SET checked = ?
             WHERE id = ?"
            checked
            id])))


(defn delete-item
  "Delete an existing item."
  [db id]
  (= [1] (db/execute!
           db
           ["DELETE FROM items
             WHERE id = ?"
            id])))


(defn read-items
  "Read in all items in the database."
  [db]
  (db/query
    db
    ["SELECT id, name, description, checked, date_created
      FROM items
      ORDER BY date_created"]))
