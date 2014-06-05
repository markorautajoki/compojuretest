(ns compojurepoc.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [compojurepoc.util :refer [json-response]]))

(defn testdata []
  (json-response {:foo "dkfjsdlfjdsklj"
                  :bar "foo"}))

(defroutes app-routes
           (GET "/" [] (resp/redirect "/index.html"))
           (GET "/api/testdata" [] (testdata))
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn foobar []
  (println "moi"))