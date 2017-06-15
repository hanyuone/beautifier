(ns beautifier.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io])
  (:gen-class))

(def gap (atom 1))

;;; Private functions

;; Gets index of first item that satisfies pred:
;;    (first-index-of even? [1 2 3])
;; => 1
(defn- first-index-of [pred coll]
  (loop [a 0]
    (cond
      (= a (count coll)) nil
      (pred (nth coll a)) a
      :else (recur (inc a)))))

;; Add spaces to the right until the string reaches
;; a certain length:
;;    (right-pad "abcd" 7)
;; => "abcd   "
(defn- right-pad [string length]
  (format (str "%-" length "s") string))

;;; Public functions

;; Set the gap between the longest line and the
;; special characters
(defn set-gap [n]
  (if (< n 1)
    (throw (Exception. "Invalid gap size!"))
    (reset! gap n)))

;; "Beautifies" Java/C#/C++ (any C-like language) code
(defn beautify [input output]
  ;; Opens file
  (let [lines
        (with-open [reader (io/reader input)]
          (doall (line-seq reader)))]
    ;; Main loop
    (loop [stripped []
           endings []
           ind 0]
      (if (= ind (count lines))
        ;; Spits out content to the output file
        ;; when the loop is finished.
        (spit output
          (apply str
            (let [new-stripped
                  (for [a (range (count stripped))]
                    (let [line (nth lines (first (nth stripped a)))
                          start-spaces
                          (subs line 0
                            (first-index-of
                              #(not= \space %)
                              line))]
                      (str start-spaces (second (nth stripped a)))))]
              (for [b (range (count new-stripped))]
                (str
                 (right-pad (nth new-stripped b)
                   (+ @gap (apply max (map count new-stripped))))
                 (nth endings b)
                 "\n")))))
        ;; Separates special characters from the body at the beginning
        ;; and end
        (let [line-no-ws (s/trim (nth lines ind))
              previous
              (let [f-index
                    (first-index-of
                      #(not (some #{%} "{};"))
                      line-no-ws)]
                (subs line-no-ws 0
                  (if (nil? f-index) (count line-no-ws) f-index)))
              next
              (if (= previous line-no-ws) ""
                (let [f-index
                      (first-index-of
                        #(not (some #{%} "{};"))
                       (reverse line-no-ws))]
                  (subs line-no-ws
                    (- (count line-no-ws) f-index))))
              stripped-line
              (subs line-no-ws
                (count previous) (- (count line-no-ws) (count next)))]
          ;; Append removed characters at the start to the end of
          ;; the next line, and place removed characters at the end
          ;; to the start of the next line.
          (if (= stripped-line "")
            (recur
              stripped
              (assoc endings
                (dec (count endings)) (str (last endings) previous))
              (inc ind))
            (recur
              (conj stripped [ind stripped-line])
              (let [new-endings (conj endings next)]
                (if (not= previous "")
                  (assoc new-endings
                    (- (count new-endings) 2)
                    (str (nth new-endings (- (count new-endings) 2)) previous))
                  new-endings))
              (inc ind))))))))

;; Main function
(defn -main [& args]
  (set-gap 10)
  (beautify "resources/test.java" "resources/beautiful.java"))
