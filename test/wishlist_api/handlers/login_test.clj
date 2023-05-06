(ns wishlist-api.handlers.login-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.login :refer [login]]))


(deftest login-test

  (testing "given no grant_type should return error"
    (is (thrown-with-msg?
          IllegalArgumentException #"Field grant_type is required"
          (login nil))))

  (testing "given invalid grant_type should return error"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid grant_type: grant_type"
          (login {:json-params {:grant_type "grant_type"}}))))

  (testing "given get-email-code grant_type should return error when no email received"
    (is (thrown-with-msg?
          IllegalArgumentException #"Email is required"
          (login {:json-params {:grant_type "get-email-code"}}))))

  (testing "given get-email-code grant_type should return error when invalid email"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid email"
          (login {:json-params {:grant_type "get-email-code" :email "some-email"}}))))

  (testing "given get-email-code grant_type should return success when valid email"
    (let [result (login {:json-params {:grant_type "get-email-code" :email "email@example.co"}})]
      (is (= 201 (:status result)))
      (is (contains? (:body result) :vault)))))
