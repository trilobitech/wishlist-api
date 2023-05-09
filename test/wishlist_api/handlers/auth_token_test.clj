(ns wishlist-api.handlers.auth-token-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [matcher-combinators.test :refer [thrown-match?]]
    [wishlist-api.handlers.auth.token :refer [auth-token]]))


(deftest auth-token-test

  (testing "given no grant_type should return error"
    (is (thrown-match?
          clojure.lang.ExceptionInfo
          {:type :bad-request
           :message #"Field grant_type is required"
           :domain :application}
          (auth-token nil))))


  (testing "given invalid grant_type should return error"
    (is (thrown-match?
          clojure.lang.ExceptionInfo
          {:type :bad-request
           :message #"Invalid grant_type: grant_type"
           :domain :application}
          (auth-token {:json-params {:grant_type "grant_type"}})))))
