(ns compojurepoc.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [clojure.set :as s]
            [compojure.route :as route]
            [compojure.core :as c]
            [clojure.walk :as w]
            [ring.util.response :as resp]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [compojurepoc.util :refer [json-response]]))

(def question-data [{:question "Milloin voit aloittaa ajokortin ajamisen?"
                     :questionid 0
                     :answers [{:text "17-vuotiaana" :correct false :answerindex 0}
                               {:text "17,5-vuotiaana" :correct false :answerindex 1}
                               {:text "18-vuotiaana" :correct true :answerindex 2}]}
                    {:question "Mitä ajoneuvoa B-luokan ajokortti oikeuttaa ajamaan?"
                     :questionid 1
                     :answers [{:text "Henkilöautoa" :correct true :answerindex 0}
                               {:text "Moottoripyörää" :correct false :answerindex 1}
                               {:text "Kuorma-autoa" :correct false :answerindex 2}]}
                    {:question "Mitä ajoneuvoa BC-luokan ajokortti oikeuttaa ajamaan?"
                     :questionid 2
                     :answers [{:text "Moottoripyörää" :correct true :answerindex 0}
                               {:text "Linja-autoa" :correct false :answerindex 1}
                               {:text "Kuorma-autoa" :correct false :answerindex 2}]}
                    {:question "Monellekko henkilölle rekisteröityä ajoneuvoa saa B-kortilla ajaa?"
                     :questionid 3
                     :answers [{:text "Kuljettaja + 5 henkilöä" :correct true :answerindex 0}
                               {:text "Kuljettaja + 7 henkilöä" :correct false :answerindex 1}
                               {:text "Kuljettaja + 8 henkilöä" :correct false :answerindex 2}]}])

(defn get-correct-answer [answer]
  (let [correct-answer (first (filter :correct (:answers answer)))]
    {:questionid (:questionid answer)
     :answerindex (:answerindex correct-answer)}))

(defn get-correct-answers []
  (map get-correct-answer question-data))

(defn correct-answer? [answer correct-answers]
  (let [correct-answer-index (-> (filter #(= (:questionid answer) (:questionid %)) correct-answers)
                                 first
                                 :answerindex)
        answer-index (:answerindex answer)]
    (=  correct-answer-index answer-index)))

(defn check-answers [answers]
  (let [all-correct-answers (get-correct-answers)
        correct-answers (filter #(correct-answer? % all-correct-answers) answers)
        wrong-answers (filter #(not (correct-answer? % all-correct-answers)) answers)]
    {:correct correct-answers
     :wrong wrong-answers}))

(defn strip-correct-answers [answer]
  (dissoc answer :correct))

(defn format-questions []
  (map #(assoc % :answers (map strip-correct-answers (:answers %))) question-data))

(defn testdata []
  (json-response (format-questions)))

(defn site-routes []
  (c/routes
    (GET "/" [] (resp/redirect "/index.html"))
    (GET "/api/testdata" [] (testdata))
    (POST "/api/checkdata" [answers] (json-response (check-answers answers)))
    (route/resources "/")
    (route/not-found "Not Found")))

(def app
  (->
    (handler/site
    (-> (site-routes)
        wrap-keyword-params
        wrap-json-params
        wrap-params))))