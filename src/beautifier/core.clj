(ns beautifier.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io])
  (:gen-class))

;;; Private variables/functions

;; Atom to define the gap between special characters
;; and rest of code
(def ^:private gap (atom 10))
;; Atom to define if blank lines are to be included
(def ^:private blank-lines? (atom false))

(defn ^:private first-index-of
  "Gets the index of the first item in coll
  that satisfies pred:
     (first-index-of even? [1 2 3])
  => 1"
  [pred coll]
  (loop [a 0]
    (cond
      (= a (count coll)) nil
      (pred (nth coll a)) a
      :else (recur (inc a)))))

(defn ^:private right-pad
  "Right-pads a string until it is greater than or
  equal to a given length:
     (right-pad \"abcd\" 7)
  => \"abcd   \""
  [string length]
  (loop [new-str string]
    (if (>= (count new-str) length) new-str
      (recur (str new-str " ")))))

;;; Public functions

(defn set-gap
  "Sets the gap between the longest line and the
  special characters."
  [n]
  (if (< n 1)
    (throw (Exception. "Invalid gap size!"))
    (reset! gap n)))

(defn include-blanks
  "Includes blank lines."
  []
  (reset! blank-lines? true))

(defn beautify
  "Takes in a list of lines of a program in a C-like language
  (e.g. Java, C#, C++) and proceeds to 'beautify' it."
  [lines]
  ;; Main loop
  (loop [stripped []
         endings []
         ind 0]
    (if (= ind (count lines))
      ;; Spits out content to the output file
      ;; when the loop is finished.
      (s/join
        ;; Defines new list, in which all the lines have whitespace
        ;; at the front taken into account.
        (let [new-stripped
              (for [a (range (count stripped))]
                (let [line (nth lines (first (nth stripped a)))
                      start-spaces
                      (let [first-index
                            (first-index-of
                              #(not= \space %)
                              line)]
                        (if (nil? first-index) line
                          (subs line 0 first-index)))]
                  (str start-spaces (second (nth stripped a)))))]
          (for [b (range (count new-stripped))]
            ;; Right-pads string.
            (str
              (right-pad (nth new-stripped b) (+ @gap (apply max (map count new-stripped))))
              (nth endings b)
              "\n"))))
      ;; Separates special characters from the body at
      ;; the beginning and end
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
              (subs line-no-ws
                (- (count line-no-ws)
                  (first-index-of
                    #(not (some #{%} "{};"))
                   (reverse line-no-ws)))))
            stripped-line
            (s/trim
              (subs line-no-ws
                (count previous) (- (count line-no-ws) (count next))))]
        ;; Append removed characters at the start to the end of
        ;; the next line, and place removed characters at the end
        ;; to the start of the next line.
        (if (and (= stripped-line "") (not (and @blank-lines? (= line-no-ws ""))))
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
            (inc ind)))))))

(defn beautify-file
  "Beautifies in-file, and then spits the output to another
  file. in-file and out-file must be valid file URIs."
  [in-file out-file]
  (let [lines
        (with-open [reader (io/reader in-file)]
          (doall (line-seq reader)))]
    (spit out-file
      (beautify lines))))

(defn beautify-folder
  "Beautifies an entire folder. in-file and out-file
  must be valid folder URIs."
  [in-folder out-folder]
  (let [files
        (map io/as-relative-path
          (rest (file-seq (io/file in-folder))))]
    (println files)
    (loop [ind 0]
      (when-not (= ind (count files))
        (do
          (let [file (nth files ind)
                new-file
                (str out-folder "\\"
                  (subs file (inc (count in-folder))))]
            (println file new-file)
            (beautify-file file new-file))
          (recur (inc ind)))))))

(defn -main [& args]
  (include-blanks)
  (beautify-folder "resources" "foo"))
