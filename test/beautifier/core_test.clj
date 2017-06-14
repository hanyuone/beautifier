(ns beautifier.core-test
  (:require [clojure.test :refer :all]
            [beautifier.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= (strip-ws "    abcd e   ") "abcd e"))))
