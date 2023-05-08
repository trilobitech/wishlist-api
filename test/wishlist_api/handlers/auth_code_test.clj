(ns wishlist-api.handlers.auth-code-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.auth-code :refer [auth-code]]))


(deftest auth-code-test

  (testing "should return error when no email received"
    (is (thrown-with-msg?
          IllegalArgumentException #"Email is required"
          (auth-code {:json-params {}}))))


  (testing "should return error when invalid email"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid email"
          (auth-code {:json-params {:email "some-email"}}))))


  (testing "should return success when valid email"
    (let [result (auth-code {:json-params {:email "email@example.co"}})]
      (is (= 201 (:status result)))
      (is (contains? (:body result) :vault)))))
