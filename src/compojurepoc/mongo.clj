(ns compojurepoc.mongo
  (:require [monger.core :as mg])
  (:require [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress])
  (:import org.bson.types.ObjectId))

;;;;;;;;;;;;;;; initialize monodb connection ;;;;;;;;;;;;;;;;;;;;;
(def conn (atom  (mg/connect)))
(def kultu-db (atom  (mg/get-db @conn "kultu")))
;(reset! conn (mg/connect))

;;;;;;;;;;;;;;; simple message protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn return-m [success message & oid]
  {:_id oid :success success :message message})


;;;;;;;;;;;;;;;; remote operations ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    

(defn add-question [question]
  (let [ins #(mc/insert @kultu-db "questions" question)]
    (cond
     (empty? question) (return-m false "Todo is invalid")
     (.getError (ins)) (return-m false "Error occured writing to the database")
     :else (return-m true ""))))

(defn find-questions []
  (let [
        res (mc/find-maps @kultu-db "questions" )]
    (return-m true (map #(dissoc % :_id) res))))
