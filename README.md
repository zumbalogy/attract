https://fract-attract.herokuapp.com/

This is a fractal and attractor drawing playground thing written in clojure/clojurescript/reagent/d3.

different functions are used for drawing. clicking and moving the mouse changes the constants used in the functions.

use the number keys to change what function is used. some functions do not use all of a b c and d.

some of the functions are attractors and/or take a coordinate and return a coordinate. By feeding it to itself over and over
again and plotting each step, funny shapes emerge.

Right now, the color is determined by the x and y distance between the previous point and the current one
being drawn.

Functions subject to change, and I want to add some more.

If you make any cool looking ones, or know any related links/projects, please send them this way!

Good bit of inspiration from: https://youtu.be/vLlbEZt-3j0

# Keyboard Shortcuts

click resets a and b

spacebar clear canvas

enter opens a tab with a png snapshot of the canvas

left and right arrow keys rotate where new 'paint' is placed

up and down arrow keys zoom in and out, causing new 'paint' to be bigger or smaller

a reset a

b reset b

[ sets the drawing mode to lighten

] sets the drawing mode to darken

1 clifford http://paulbourke.net/fractals/clifford/
```clojure
x2 (+ (Math.sin (* @a y)) (* @c (Math.cos (* @a x))))
y2 (+ (Math.sin (* @b x)) (* @d (Math.cos (* @b y))))
```

2 dejong http://paulbourke.net/fractals/peterdejong/
```clojure
x2 (- (Math.sin (* @a y) (Math.cos (* @b x))))
y2 (- (Math.sin (* (+ @c 1) x)) (Math.cos (* (+ @d 1) y)))
```

3 svensson bottom of http://paulbourke.net/fractals/peterdejong/
```clojure
x2 (- (* @d (Math.sin (* @a x))) (Math.sin (* @b y)))
y2 (- (* @c (Math.cos (* @a x))) (Math.cos (* @b y)))
```

4 lorenz https://en.wikipedia.org/wiki/Lorenz_system
```clojure
x2 (+ x (* 0.01 @b (- y x)))
y2 (+ y (* 0.01 (- (* x (- 28 @z)) y)))
z2 (+ @z (* 0.01 (- (* y x) (* @d @z))))
```

5 duffing http://paulbourke.net/fractals/duffing/
```clojure
h (/ @a 20)
x2 (+ x (* h y))
y2 (+ y (* h (+ (- x (* x x x) (* @c y)) (* @d (Math.cos @z)))))
z2 (+ @z h)
```

6 sierpinski http://www.oftenpaper.net/sierpinski.htm
```clojure
x2 (/ x 2)
y2 (/ y 2)]
(rand-nth [[x2 y2]
           [(+ x2 1) y2]
           [x2 (- y2 1)]
           [x2 (+ y2 1)]
           [(- x2 1) y2]])
```

7 aizawa http://www.algosome.com/articles/aizawa-attractor-chaos.html
```clojure
t 0.01
e (/ @c 2)
a 0.95
l 0.6
d (* 3.7 @d)
b 0.7
c 0.1

czxxx (* c @z x x x)
ez (+ 1 (* e @z))
xxyy (+ (* x x) (* y y))
z3 (/ (* @z @z @z) 3)
az (* a @z)

z2 (+ @z (* t (- (+ l az czxxx) z3 (* ez xxyy))))]
x2 (+ x (* t (- (* (- @z b) x) (* d y))))
y2 (+ y (* t (+ (* d x) (* y (- @z b)))))

```

# Further Reading
https://github.com/mattdesl/color-wander

http://chaoticatmospheres.com/mathrules-strange-attractors

http://www.bentamari.com/attractors.html

http://michaelalynmiller.com/blog/2013/06/18/epitrochoids-in-clojurescript/

https://github.com/cjlarose/de-jong

https://en.wikipedia.org/wiki/Barnsley_fern

https://en.wikipedia.org/wiki/Newton_fractal

https://en.wikipedia.org/wiki/Brownian_tree

http://www.scholarpedia.org/article/Duffing_oscillator

http://softology.com.au/tutorials/attractors2d/tutorial.htm

https://en.wikipedia.org/wiki/Ikeda_map

http://paulbourke.net/fractals/ikeda/

http://paulbourke.net/fractals/thorn/

http://paulbourke.net/fractals/henonphase/

http://paulbourke.net/fractals/lyapunov/

http://www.chaoscope.org/doc/attractors.htm

http://chaoticatmospheres.com/mathrules-strange-attractors

http://sprott.physics.wisc.edu/pubs/paper317.htm

https://explord.com/experiments/strange-attractors/

http://pixellab.jozefmaxted.co.uk/#/attractors

http://rectangleworld.com/demos/ChaosGame2/chaos_game_2.html

https://github.com/jaybosamiya/chaos-game

https://github.com/alisey/explord.com/blob/master/experiments/strange-attractors/js/attractor.js

http://weavesilk.com/

https://johnhw.github.io/umap_primes/index.md.html

# TODO
Performance tuning and maybe getting rid of d3

better seirpinski stuff

look at http://mathworld.wolfram.com/IceFractal.html and
https://twitter.com/math_gif/status/795168214875394048
http://mate.dm.uba.ar/~umolter/materias/multifractal-2014/Papers/Paper-elegido%20por%20Fernando.pdf

more functions

prettier legend

better code cleaning and maybe a file for helpers (like one for js/console.log)

work on screen sizes besides just my own, mobile and test other browsers

more better examples

fix bug where snapshot sometimes has white background (this is caused by clearning the canvas, so
  i should reset it to black i think when i do that)

maybe be able to have a function do its own color (or at least get better lorenz colors)

# Examples
![example](https://raw.githubusercontent.com/zumbalogy/attract/master/resources/public/pictures/Screenshot%20from%202016-02-07%2011-43-58.png)
![example](https://raw.githubusercontent.com/zumbalogy/attract/master/resources/public/pictures/Screenshot%20from%202016-02-06%2013-59-19.png)
![example](https://raw.githubusercontent.com/zumbalogy/attract/master/resources/public/pictures/Screenshot%20from%202016-02-14%2011-51-02.png)
![example](https://raw.githubusercontent.com/zumbalogy/attract/master/resources/public/pictures/Screenshot%20from%202016-02-07%2000-43-51.png)
![example](https://raw.githubusercontent.com/zumbalogy/attract/master/resources/public/pictures/Screenshot%20from%202016-02-07%2000-20-41.png)
![example](https://raw.githubusercontent.com/zumbalogy/attract/master/resources/public/pictures/IMG_20160209_161917.jpg)
