(ns wishlist-api.handlers.hello-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.hello :refer [hello-world]]))


(deftest hello-test
  (testing "should say hello"
    (is (= ((hello-world nil) :body) "Hello World!"))))
