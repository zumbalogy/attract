(ns attract.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [attract.painter :as paint]
            [attract.mouse :as mouse]))

(def wrapper
  (with-meta identity
    {:component-did-mount paint/init}))

(defn home-page []
  (fn []
    [wrapper [:canvas#home-canvas {:on-click paint/reset-a-b
                                   :height 800
                                   :width 1550}]]))

(defn current-page []
  [:div [(session/get :current-page)]])

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
