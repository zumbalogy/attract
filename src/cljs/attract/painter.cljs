(ns attract.painter
  (:require [reagent.core :as r]
            [cljsjs.d3]
            [attract.mouse :as mouse]))

(def x (atom 1))
(def y (atom 1))
(def z (atom 1))
(def old-x (atom 1))
(def old-y (atom 1))

(def a (r/atom 0.003))
(def b (r/atom 28))
(def c (r/atom 10))
(def d (r/atom 2.6666))

(defn round [x]
  (/ (Math.floor (* 100 x)) 100))

(defn clifford [[x y]]
  (let [x2 (+ (Math.sin (* @a y)) (* @c (Math.cos (* @a x))))
        y2 (+ (Math.sin (* @b x)) (* @d (Math.cos (* @b y))))]
    [x2 y2]))

(defn dejong [[x y]]
  ; all constants have to be between pi and negative pi
  (let [x2 (- (Math.sin (* @a y) (Math.cos (* @b x))))
        y2 (- (Math.sin (* (+ @c 1) x)) (Math.cos (* (+ @d 1) y)))]
    [x2 y2]))

(defn svensson [[x y]]
  (let [x2 (- (* @d (Math.sin (* @a x))) (Math.sin (* @b y)))
        y2 (- (* @c (Math.cos (* @a x))) (Math.cos (* @b y)))]
    [x2 y2]))

(defn lorenz [[x y]]
  (let [x2 (+ x (* 0.01 @b (- y x)))
        y2 (+ y (* 0.01 (- (* x (- 28 @z)) y)))
        z2 (+ @z (* 0.01 (- (* y x) (* @d @z))))]
    (reset! z z2)
    [x2 y2]))

(defn aizawa [[x y]]
  (let [t 0.01
        ; e 0.25
        e (/ @c 2)
        a 0.95
        l 0.6
        ; d 3.5
        d (* 3.7 @d)
        b 0.7
        c 0.1
        x2 (+ x (* t (- (* (- @z b) x) (* d y))))
        y2 (+ y (* t (+ (* d x) (* y (- @z b)))))
        czxxx (* c @z x x x)
        ez (+ 1 (* e @z))
        xxyy (+ (* x x) (* y y))
        z3 (/ (* @z @z @z) 3)
        az (* a @z)
        z2 (+ @z (* t (- (+ l az czxxx) z3 (* ez xxyy))))]
    (reset! z z2)
    [x2 y2]))

(defn duffing [[x y]]
  (let [h (/ @a 20)
        x2 (+ x (* h y))
        y2 (+ y (* h (+ (- x (* x x x) (* @c y)) (* @d (Math.cos @z)))))
        z2 (+ @z h)]
    (reset! z z2)
    [x2 y2]))

(defn triz [[x y]]
  (let [x2 (/ x 2)
        y2 (/ y 2)]
    (rand-nth [[x2 y2]
              ;  [(+ x2 0.5) (+ y2 0.86)]
               [(+ x2 1) y2]
               [x2 (- y2 1)]
               [x2 (+ y2 1)]
               [(- x2 1) y2]])))

; (def attract-fn (r/atom clifford))
(def attract-fn (r/atom aizawa))

(defn clean-xyz []
  (reset! x 1)
  (reset! y 1)
  (reset! z 1))

(defn reset-a-b []
  (clean-xyz)
  (reset! a (rand 3))
  (reset! b (rand 3)))

(defn reset-c-d [x y]
  (reset! c (/ x 900))
  (reset! d (/ y 900)))

(defn reset-x-y []
  (let [pair (@attract-fn [@x @y])]
    (reset! old-y @y)
    (reset! old-x @x)
    (reset! x (first pair))
    (reset! y (last pair))))

(defn gen-color [x y]
  (let [r (int (* 100 (Math.abs x)))
        g 40 ; think about doing something with z index
        b (int (* 100 (Math.abs y)))
        a 0.4]
    (str "rgba(" r "," g "," b "," a ")")))

(defn clear-canvas [ctx]
  (clean-xyz)
  (.save ctx)
  (.setTransform ctx 1 0 0 1 0 0)
  (.clearRect ctx 0 0 1550 800) ; TODO: make dynamic
  (.restore ctx))

(defn get-ctx [target-id]
  (let [canvas (js/d3.select target-id)
        node (.node canvas)
        ctx (.getContext node "2d")]
    (set! (.-globalCompositeOperation ctx) "lighter")
    (set! (.-fillStyle ctx) "black")
    (.fillRect ctx -1000 -1000 100000 1000000)
    (.translate ctx 800 400)
    (.scale ctx 150 150)
    ctx))

(defn set-paint-fn [fn]
  (clean-xyz)
  (reset! attract-fn fn))

(defn key-handler [ctx e]
  (case (.-keyCode e)
    13 (.open js/window (.toDataURL js/canvas "image/png"))
    32 (clear-canvas ctx)
    37 (.rotate ctx 0.1)
    39 (.rotate ctx -0.1)
    40 (.scale ctx 0.97 0.97)
    38 (.scale ctx 1.03 1.03)
    49 (set-paint-fn clifford)
    50 (set-paint-fn dejong)
    51 (set-paint-fn svensson)
    52 (set-paint-fn lorenz)
    53 (set-paint-fn duffing)
    54 (set-paint-fn triz)
    55 (set-paint-fn aizawa)
    97 (reset! a (rand 3))
    98 (reset! b (rand 3))
    219 (set! (.-globalCompositeOperation ctx) "lighter")
    221 (set! (.-globalCompositeOperation ctx) "source-over")
    "default"))

(defn time-fn [ctx]
  (reset-c-d @mouse/x @mouse/y)
  (doseq [z (range 10000)]
    (set! (.-fillStyle ctx) (gen-color @old-x @old-y))
    (.fillRect ctx @x @y 0.01 0.01)
    (reset-x-y))
  false)

(defn stats []
  [:p.stats "a " @a " b " @b " c " @c " d " @d])

(defn init [& args]
  (reset-a-b)
  (let [ctx (get-ctx "#canvas")]
    (.addEventListener js/window "keydown" #(key-handler ctx %))
    (js/d3.timer #(time-fn ctx))))
