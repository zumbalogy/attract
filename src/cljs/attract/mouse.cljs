(ns attract.mouse)

(def x (atom 0))
(def y (atom 0))

(defn update-mouse [e]
  (reset! y (.-pageY e))
  (reset! x (.-pageX e)))

(set! (.-onmousemove js/document) update-mouse)
