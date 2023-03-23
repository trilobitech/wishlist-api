(ns wishlist-api.handlers.login-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.login :refer [login]]))


(deftest login-test

  (testing "should return error when no method received"
    (is (thrown-with-msg?
          IllegalArgumentException #"Login method is required"
          (login nil))))

  (testing "should return error when invalid method"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid login method: method"
          (login {:json-params {:method "method"}})))))
