(ns wishlist-api.core-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.core :refer [hello-world]]))


(deftest hello-test
  (testing "should say hello"
    (is (= ((hello-world nil) :body) "Hello World!"))))
