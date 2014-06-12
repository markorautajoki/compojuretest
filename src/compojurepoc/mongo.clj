(ns compojurepoc.mongo
  (:use
        [monger.core :only [connect! set-db! get-db]]
        [monger.collection :only [insert find-maps find-one find-one-as-map update]]
        )
  (:require [monger.core :as mg])
  (:import org.bson.types.ObjectId))

;;;;;;;;;;;;;;; initialize monodb connection ;;;;;;;;;;;;;;;;;;;;;
(connect!)
(set-db! (get-db "kultu"))


;;;;;;;;;;;;;;; simple message protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn return-m [success message & oid]
  {:_id oid :success success :message message})


;;;;;;;;;;;;;;;; remote operations ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    

(defn add-question [question]
  (let [ins #(insert "questions" question)]
    (cond
     (empty? question) (return-m false "Todo is invalid")
     (.getError (ins)) (return-m false "Error occured writing to the database")
     :else (return-m true ""))))

(defn find-questions []
  (let [res (find-maps "questions" )]
    (return-m true (map #(dissoc % :_id) res))))
