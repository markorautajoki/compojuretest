(ns compojurepoc.util
  (:require
    [cheshire.core :as cheshire]))

(defn json-response
  [data]
  {:status 200
   :body (cheshire/generate-string data)
   :headers {"Content-Type" "application/json"}})

