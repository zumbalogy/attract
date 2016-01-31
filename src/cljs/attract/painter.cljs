(ns attract.painter
  (:require [cljsjs.d3]))

(def x (atom 1))
(def y (atom 1))

(def a 2.0)
(def b -3.0)
(def c (atom (rand)))
(def d (atom (rand)))

; maybe make color dependant on how far away this new point is from prev on

(defn dejong [[x y]]
  (let [x2 (+ (Math.sin (* a y)) (* @c (Math.cos (* a x))))
        y2 (+ (Math.sin (* b x)) (* @d (Math.cos (* b y))))]
    [x2 y2]))

(defn reset-settings [click]
  (reset! c (/ (.-pageX click) 1000))
  (reset! d (/ (.-pageY click) 1000)))

(defn reset-x-y []
  (let [pair (dejong [@x @y])]
    (reset! x (first pair))
    (reset! y (last pair))))

(defn gen-color [x y]
  (let [r (int (* 100 (Math.abs x)))
        g 40
        b (int (* 100 (Math.abs y)))
        a 0.5]
    (str "rgba(" r "," g "," b "," a ")")))

(defn get-ctx [target-id]
  (let [canvas (js/d3.select target-id)
        node (.node canvas)
        ctx (.getContext node "2d")]
    (.attr canvas "width" 1550)
    (.attr canvas "height" 800)
    (set! (.-globalCompositeOperation ctx) "lighter")
    (.translate ctx 800 400)
    (.scale ctx 150 150)
    ctx))

(defn draw-dejong [click]
  (reset-settings click)
  (let [ctx (get-ctx "#home-canvas")]
    (doseq [z (range 50000)]
      (set! (.-fillStyle ctx) (gen-color @x @y))
      (.fillRect ctx @x @y 0.01 0.01)
      (reset-x-y))))
