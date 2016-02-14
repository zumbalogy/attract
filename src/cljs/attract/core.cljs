(ns attract.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [attract.painter :as paint]
            [attract.mouse :as mouse]))

(def wrapper
  (with-meta identity
    {:component-did-mount paint/init}))

(def canvas
  [:canvas#canvas {:on-click paint/reset-a-b
                   :height 800
                   :width 1550}])

(def legend
  [:p.legend "click resets a and b,
              spacebar clear canvas,
              a reset a,
              b reset b,
              [ lighten,
              ] darken,
              1 clifford,
              2 dejong,
              3 svensson,
              4 lorenz,
              5 duffing,
              6 sierpinski "
              [:a.github {:href "https://github.com/zumbalogy/attract"} "GitHub"]])

(defn home-page []
  (fn []
    [:div
      legend
      [paint/stats]
      [wrapper canvas]]))

(defn current-page []
  [:div [(session/get :current-page)]])

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(defn mount-root []
  (r/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))
