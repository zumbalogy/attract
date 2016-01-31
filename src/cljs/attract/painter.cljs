(ns attract.painter
  (:require [cljsjs.d3]))

(defn draw-dejong [target-id]
  (js/console.log (get-ctx target-id)))

(defn get-ctx [target-id]
  (let [canvas (js/d3.select target-id)
        node (.node canvas)
        ctx (.getContext node "2d")]
    ctx))

(defn dejong [[x y]]
  (let [a 2.01
        b -3.00
        c 1.61
        d -0.33
        x2 (+ (Math.sin (* a y)) (* c (Math.cos (* a x))))
        y2 (+ (Math.sin (* b x)) (* d (Math.cos (* b y))))]
    [x2 y2]))



  ; width = 1000
  ; height = 1000
  ; canvas = d3.select('body').append('canvas').attr('width', width).attr('height', height)
  ; color = d3.scale.linear().domain([0, 6]).range(['blue', 'red']).interpolate(d3.interpolateHcl)
  ; context = canvas.node().getContext('2d')
  ;
  ; d3.timer ->
  ;   context.save()
  ;   context.globalCompositeOperation = 'lighter'
  ;   context.translate(width / 2, height / 2)
  ;   context.scale(150, 150)
  ;   i = 0
  ;   while i < 2000
  ;     r = Math.floor(Math.abs(x) * 100)
  ;     b = Math.floor(Math.abs(y) * 100)
  ;     context.fillStyle = "rgba(#{r}, 40, #{b}, 0.1)"
  ;     [x, y] = dejong(x, y)
  ;     context.fillRect(x, y, 0.005, 0.005)
  ;     context.stroke()
  ;     i++
  ;   context.restore())
