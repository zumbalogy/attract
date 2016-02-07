(ns attract.painter
  (:require [cljsjs.d3]
            [attract.mouse :as mouse]))

(def x (atom 0))
(def y (atom 0))
(def z (atom 0))
(def old-x (atom 1))
(def old-y (atom 1))

(def a (atom 0.003))
(def b (atom 28))
(def c (atom 10))
(def d (atom 2.6666))

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

(defn lorenz [[x y]]
  (let [
        x2 (+ x (* 0.01 @b (- y x)))
        y2 (+ y (* 0.01 (- (* x (- 28 @z)) y)))
        z2 (+ @z (* 0.01 (- (* y x) (* @d @z))))]
    (reset! z z2)
    [x2 y2]))

(defn duffing [[x y]]
  (js/console.log x y)
  (let [x2 (+ x (* 0.04 y))
        y2 (+ y (* 0.04 (+ (- x (* x x x) (* 0.2 y)) (* 0.3 (Math.cos @z)))))
        z2 (+ @z 0.04)]
    (reset! z z2)
    [x2 y2]))

(def attract-fn (atom duffing))

(defn reset-a-b []
  (reset! x 1)
  (reset! y 1)
  (reset! z 1)
  (reset! a (rand 3))
  (reset! b (rand 3)))

(defn reset-c-d [x y]
  (reset! c (/ x 900))
  (reset! d (/ y 900))
  (set! (.-title js/document) (str "c:" (round @c) "_d:" (round @d))))

(defn reset-x-y []
  (let [pair (@attract-fn [@x @y])]
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
    (set! (.-globalCompositeOperation ctx) "lighter")
    (.translate ctx 800 400)
    (.scale ctx 150 150)
    ; (.scale ctx 30 30)
    ctx))

(defn key-handler [ctx e]
  (case (.-keyCode e)
    32 (clear-canvas ctx)
    49 (reset! attract-fn clifford)
    50 (reset! attract-fn dejong)
    51 (reset! attract-fn svensson)
    52 (reset! attract-fn lorenz)
    53 (reset! attract-fn duffing)
    91 (set! (.-globalCompositeOperation ctx) "lighter")
    93 (set! (.-globalCompositeOperation ctx) "source-over")
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
