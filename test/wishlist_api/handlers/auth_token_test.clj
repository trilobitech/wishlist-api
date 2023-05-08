(ns wishlist-api.handlers.auth-token-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.auth-token :refer [auth-token]]))


(deftest auth-token-test

  (testing "given no grant_type should return error"
    (is (thrown-with-msg?
          IllegalArgumentException #"Field grant_type is required"
          (auth-token nil))))


  (testing "given invalid grant_type should return error"
    (is (thrown-with-msg?
          IllegalArgumentException #"Invalid grant_type: grant_type"
          (auth-token {:json-params {:grant_type "grant_type"}})))))
