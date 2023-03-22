(ns wishlist-api.handlers.login-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [wishlist-api.handlers.login :refer [login]]))


(deftest login-test

  (testing "should return error when no method received"
    (is (=
          (login nil)
          {:status 400
           :headers {"Content-Type" "application/json"}
           :body "{\"error-message\":\"Login method is required\"}"})))


  (testing "should return error when invalid method"
    (is (=
          (login {:json-params {:method "method"}})
          {:status 400
           :headers {"Content-Type" "application/json"}
           :body "{\"error-message\":\"Invalid login method: method\"}"}))))
