(ns wishlist-api.handlers.auth-token-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [matcher-combinators.test :refer [thrown-match?]]
    [wishlist-api.handlers.auth-token.grant-handler :refer [auth-token]]))


(deftest auth-token-test

  (testing "given no grant_type should return error"
    (is (thrown-match?
          clojure.lang.ExceptionInfo
          {:type :input-validation
           :message #"Field grant_type is required"}
          (auth-token nil))))


  (testing "given invalid grant_type should return error"
    (is (thrown-match?
          clojure.lang.ExceptionInfo
          {:type :input-validation
           :message #"Invalid grant_type: grant_type"}
          (auth-token {:json-params {:grant_type "grant_type"}})))))
