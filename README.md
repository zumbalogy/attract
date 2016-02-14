This is a fractal and attractor drawing playground thing written in clojure/clojurescript/reagent/d3.


# Keyboard Shortcuts

click resets a and b

spacebar clear canvas

a reset a

b reset b

[ sets the drawing mode to lighten

] sets the drawing mode to darken

1 clifford
```clifford
x2 (- (Math.sin (* @a y) (Math.cos (* @b x))))
y2 (- (Math.sin (* (+ @c 1) x)) (Math.cos (* (+ @d 1) y)))
```

2 dejong,

3 svensson,

4 lorenz,

5 duffing,

6 sierpinski


# TODO
Performance tuning and maybe getting rid of d3

better seirpinski stuff

more functions

prettier legend

change background colors (maybe have a option to have a changing background, or gradient or user uploaded)

open as png in new tab (need to make background color line up)
https://blogs.adobe.com/digitalmedia/2011/01/setting-the-background-color-when-generating-images-from-canvas-todataurl/
var img = canvas.toDataURL("image/png");
window.open(img, blank);

better code cleaning and maybe a file for helpers (like one for js/console.log)
