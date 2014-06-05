(defproject compojurepoc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [cheshire "5.2.0"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler compojurepoc.handler/app
         :nrepl {:start? true :port 5555}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
