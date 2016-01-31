(ns attract.painter
  (:require [cljsjs.d3]))

(def x (atom 1))
(def y (atom 1))

(defn dejong [[x y]]
  (let [a 2.01
        b -3.00
        c 1.61
        d -0.33
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
        a 0.1]
    (str "rgba(" r "," g "," b "," a ")")))

(defn get-ctx [target-id]
  (let [canvas (js/d3.select target-id)
        node (.node canvas)
        ctx (.getContext node "2d")]
    (.attr canvas "width" 1550)
    (.attr canvas "height" 800)
    (set! (.-globalCompositeOperation ctx) "lighter")
    (.translate ctx 750 400)
    (.scale ctx 100 100)
    ctx))

(defn draw-dejong [target-id]
  (let [ctx (get-ctx target-id)]
    (doall (repeatedly 1000000 (fn []
                                (set! (.-fillStyle ctx) (gen-color @x @y))
                                (.fillRect ctx @x @y 0.01 0.01)
                                (reset-x-y))))))
