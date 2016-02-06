(ns attract.painter
  (:require [cljsjs.d3]
            [attract.mouse :as mouse]))

(def x (atom 1))
(def y (atom 1))
(def old-x (atom 1))
(def old-y (atom 1))

(def a (atom 2.0))
(def b (atom -3.0))
(def c (atom (rand)))
(def d (atom (rand)))

(defn round [x]
  (/ (Math.floor (* 100 x)) 100))

(defn clifford [[x y]]
  (let [x2 (+ (Math.sin (* @a y)) (* @c (Math.cos (* @a x))))
        y2 (+ (Math.sin (* @b x)) (* @d (Math.cos (* @b y))))]
    [x2 y2]))

(defn dejong [[x y]]
  (let [x2 (- (Math.sin (* @a y) (Math.cos (* @b x))))
        y2 (- (Math.sin (* @c x)) (Math.cos (* @d y)))]
    [x2 y2]))

(defn svensson [[x y]]
  (let [x2 (- (* @d (Math.sin (* @a x))) (Math.sin (* @b y)))
        y2 (- (* @c (Math.cos (* @a x))) (Math.cos (* @b y)))]
    [x2 y2]))

(defn reset-a-b []
  (reset! a (rand 3))
  (reset! b (rand 3)))

(defn reset-c-d [x y]
  (reset! c (/ x 900))
  (reset! d (/ y 900))
  (set! (.-title js/document) (str "c:" (round @c) "_d:" (round @d))))

(defn reset-x-y []
  (let [pair (svensson [@x @y])]
    (reset! old-y @y)
    (reset! old-x @x)
    (reset! x (first pair))
    (reset! y (last pair))))

(defn gen-color [x y]
  (let [r (int (* 100 (Math.abs x)))
        g 40
        b (int (* 100 (Math.abs y)))
        a 0.4]
    (str "rgba(" r "," g "," b "," a ")")))

(defn clear-canvas [ctx]
  (.save ctx)
  (.setTransform ctx 1 0 0 1 0 0)
  (.clearRect ctx 0 0 1550 800) ; TODO: make dynamic
  (.restore ctx))

(defn get-ctx [target-id]
  (let [canvas (js/d3.select target-id)
        node (.node canvas)
        ctx (.getContext node "2d")]
    ; (set! (.-globalCompositeOperation ctx) "source-over") ; default
    (set! (.-globalCompositeOperation ctx) "lighter")
    (.translate ctx 800 400)
    (.scale ctx 150 150)
    ctx))

(defn key-handler [ctx e]
  (case (.-keyCode e)
    32 (clear-canvas ctx)
    "default"))

(defn time-fn [ctx]
  (reset-c-d @mouse/x @mouse/y)
  (doseq [z (range 10000)]
    (set! (.-fillStyle ctx) (gen-color @old-x @old-y))
    (.fillRect ctx @x @y 0.01 0.01)
    (reset-x-y))
  false)

(defn init [& args]
  (reset-a-b)
  (let [ctx (get-ctx "#home-canvas")]
    (.addEventListener js/window "keypress" #(key-handler ctx %))
    (js/d3.timer #(time-fn ctx))))
