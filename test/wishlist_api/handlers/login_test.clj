(ns wishlist-api.handlers.login-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.login :refer [login]]))


(deftest login-test

  (testing "given no method should return error"
    (is (thrown-with-msg?
          IllegalArgumentException #"Login method is required"
          (login nil))))

  (testing "given invalid method should return error"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid login method: method"
          (login {:json-params {:method "method"}}))))

  (testing "given email-code method should return error when no email received"
    (is (thrown-with-msg?
          IllegalArgumentException #"Email is required"
          (login {:json-params {:method "email-code"}}))))

  (testing "given email-code method should return error when invalid email"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid email"
          (login {:json-params {:method "email-code" :email "some-email"}}))))

  (testing "given email-code method should return error when valid email"
    (is (= {:status 204}
           (login {:json-params {:method "email-code" :email "email@example.co"}})))))
