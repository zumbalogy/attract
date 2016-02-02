(ns attract.painter
  (:require [cljsjs.d3]))

(def x (atom 1))
(def y (atom 1))

(def a (atom 2.0))
(def b (atom -3.0))
(def c (atom (rand)))
(def d (atom (rand)))

(defn clear-canvas
  [ctx width height]
  (.save ctx)
  (.setTransform ctx 1 0 0 1 0 0)
  (.clearRect ctx 0 0 width height)
  (.restore ctx))

; maybe make color dependant on how far away this new point is from prev on

(defn round [x]
  (/ (Math.floor (* 100 x)) 100))

(defn dejong [[x y]]
  (let [x2 (+ (Math.sin (* @a y)) (* @c (Math.cos (* @a x))))
        y2 (+ (Math.sin (* @b x)) (* @d (Math.cos (* @b y))))]
    [x2 y2]))

(defn reset-c-d [e]
  (set! (.-title js/document) (str "c:" (round @c) "__d:" (round @d)))
  (reset! c (/ (.-pageX e) 900))
  (reset! d (/ (.-pageY e) 900)))

(defn reset-x-y []
  ; (set! (.-title js/document) (str "a:" @a "b:" @b "c:" @c "d:" @d))
  (let [pair (dejong [@x @y])]
    (reset! x (first pair))
    (reset! y (last pair))))

(defn gen-color [x y]
  (let [r (int (* 100 (Math.abs x)))
        g 40
        b (int (* 100 (Math.abs y)))
        a 0.7]
    (str "rgba(" r "," g "," b "," a ")")))

(defn get-ctx [target-id] ; TODO: only do this once to avoid extra work
  (let [canvas (js/d3.select target-id)
        node (.node canvas)
        ctx (.getContext node "2d")]
    (.attr canvas "width" 1550) ; TODO: look into faster way to clear canvas
    (set! (.-globalCompositeOperation ctx) "lighter")
    (.translate ctx 800 400)
    (.scale ctx 150 150)
    ctx))

(defn draw-dejong [e]
  (reset-c-d e)
  (let [ctx (get-ctx "#home-canvas")]
    (doseq [z (range 80000)]
      (set! (.-fillStyle ctx) (gen-color @x @y))
      (.fillRect ctx @x @y 0.01 0.01)
      (reset-x-y))))

(defn reset-a-b [e]
  (reset! a (rand 3))
  (reset! b (rand 3))
  (draw-dejong e))
