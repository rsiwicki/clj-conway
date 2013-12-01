(ns clj-conway.core
(:import (javax.swing JPanel JFrame Timer)
         (java.awt.event ActionListener KeyListener)
         (java.awt Dimension Image)))

(def frame-width 500)
(def frame-height 500)
(def num-cells-x 101)
(def num-cells-y 101)

(defn populate [] (> (rand) 0.9))

(defn build-row [width]
  (vec (for [x (range width)] (populate))))

(defn build-matrix []
  (do (vec (for [y (range num-cells-y)] (build-row num-cells-x)))))

; Any live cell with fewer than two live neighbours dies, as if caused by under-population.
; Any live cell with two or three live neighbours lives on to the next generation.
; Any live cell with more than three live neighbours dies, as if by overcrowding.
; Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

(defn count-living-neighbours [world x y] 
  (count (filter #(get-in world (vector (+ x (first %)) (+ y (second %)))) [[-1 0] [-1 -1] [0 -1] [1 -1] [1 0] [1 1] [0 1] [-1 1]])))

(defn cell-alive [world x y] 
  (let [n (count-living-neighbours world x y) i (get-in world [x y])]
    (if i 
      (cond
        (< n 2) false
        (<= n 3) true
        (> n 3) false
        :else false)
      (cond 
        (= n 3) true
        :else false))))

(def world-1 (atom (build-matrix)))
(defn num-x-cells [world] (count (world 0)))
(defn num-y-cells [world] (count world))
(defn cell-height [world] (int (/ frame-height (num-y-cells world))))
(defn cell-width [world] (int (/ frame-width (num-x-cells world))))

;== GRAPHICS ===================


(defn clear [g] (.clearRect g 0 0 frame-width frame-height))

(defn draw-cell [g x y alive world]
  (if alive  
    (do
        (.setColor g (java.awt.Color. 255 0 0))      
        (.fillRect g (* x (cell-width world)) (* y (cell-height world)) (cell-width world) (cell-height world))
        (.setColor g (java.awt.Color. 0 0 0))
        (.drawRect g (* x (cell-width world)) (* y (cell-height world)) (cell-width world) (cell-height world)))))

(defn draw-world [world g]
  (doall (for [x (range (num-x-cells world)) y (range (num-y-cells world))]
        (draw-cell g x y (get-in world [x y]) world))))

(defn world-panel [world]
  (proxy [JPanel ActionListener] []
    (paintComponent [g] ; <label id="code.game-panel.paintComponent"/>
      (proxy-super paintComponent g)      
      (draw-world @world-1 g))
    (actionPerformed [e]
      (.repaint this))))

;== WORLD GEN ================

(defn build-next-gen-row [width current-world x]
  (vec (for [y (range width)] (cell-alive current-world x y))))

(defn build-next-gen-world [current-world]
  (do 
    (vec (for [x (range num-cells-x)] (build-next-gen-row num-cells-y current-world x)))))

; =============================

(defn evolve [world cntdwn g]
  (when (> cntdwn 0)
    (do  
      (let [newworld (build-next-gen-world world)]
        (reset! world-1 newworld)
        (.repaint g) ; -- eek!
        (Thread/sleep 50)
        (recur newworld (dec cntdwn) g)))))

(defn main []  
  (let [world @world-1
        fr (JFrame. "Conway's Game of Life")   
        panel (world-panel world)]
  (doto panel
      (.setPreferredSize (Dimension. frame-width frame-height)))
  (doto fr
    (.add panel)
    (.setSize (java.awt.Dimension. frame-width frame-height))
    (.setVisible true))
    (.start (Thread. (evolve @world-1 3000 fr)))))

