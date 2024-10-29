(ns counter
    (:require [ring.adapter.jetty :as jetty]
              [ring.middleware.params :refer [wrap-params]]
              [hiccup.core :refer [html]]))

;; Atom to store the counter state
(def counter (atom 0))

;; Function to generate the HTML content
(defn counter-page [count]
  (html
   [:html
    [:head
     [:title "SSR Counter"]]
    [:body
     [:h1 "SSR Counter"]
     [:p "Current Count: " count]
     [:form {:method "post"}
      [:button {:type "submit" :name "action" :value "increment"} "Increment"]
      [:button {:type "submit" :name "action" :value "decrement"} "Decrement"]]]]))

;; Function to handle the counter update
(defn counter-handler [request]
  (let [action (get-in request [:params "action"])]
    (case action
          "increment" (swap! counter inc)
          "decrement" (swap! counter dec)
          nil))  ; do nothing if no action is given
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (counter-page @counter)})

;; Main application with route and middleware
(def app
  (wrap-params
   (fn [request]
     (if (= (:request-method request) :post)
       (counter-handler request)
       {:status 200
        :headers {"Content-Type" "text/html"}
        :body (counter-page @counter)}))))

;; Start the server
(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false}))
