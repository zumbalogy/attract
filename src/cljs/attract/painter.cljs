(ns attract.painter
  (:require [cljsjs.d3]))

(def x (atom 1))
(def y (atom 1))

(def a (rand))
(def b (rand))
(def c (rand))
(def d (rand))

; maybe make color dependant on how far away this new point is from prev one

(defn dejong [[x y]]
  (let [a 2.01
        b -3.00
        x2 (+ (Math.sin (* a y)) (* c (Math.cos (* a x))))
        y2 (+ (Math.sin (* b x)) (* d (Math.cos (* b y))))]
    [x2 y2]))

(defn reset-x-y []
  (let [pair (dejong [@x @y])]
    (reset! x (first pair))
    (reset! y (last pair))))

(defn gen-color [x y]
  (let [r (int (* 100 (Math.abs x)))
        g 40
        b (int (* 100 (Math.abs y)))
        a 1]
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

(defn draw-dejong [target-id]
  (let [ctx (get-ctx target-id)]
    (doseq [z (range 100000)]
      (set! (.-fillStyle ctx) (gen-color @x @y))
      (.fillRect ctx @x @y 0.01 0.01)
      (reset-x-y))))
