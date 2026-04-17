(ns fuxx.ipc-test
  (:require [clojure.test :refer [deftest is testing]]
            [fuxx.schema :as schema]))

(def capability-call-schema
  [:map
   [:capability :keyword]
   [:args :map]
   [:caller {:optional true} :keyword]
   [:request-id {:optional true} :string]])

(def capability-result-schema
  [:map
   [:ok :boolean]
   [:value {:optional true} any?]
   [:error {:optional true}
    [:map
     [:code :keyword]
     [:message :string]]]])

(schema/register! :test/ipc-call   {:schema capability-call-schema})
(schema/register! :test/ipc-result {:schema capability-result-schema})

;; ── Simulated capability round-trips ─────────────────────────────

(defn mock-invoke
  "Simulates a capability handler returning ok/error without hitting Rust."
  [{:keys [capability args]}]
  (case capability
    :fs/read  (if (:path args)
                {:ok true  :value {:text "hello fuxx"}}
                {:ok false :error {:code :fs/read-error :message "missing path"}})
    :fs/write (if (and (:path args) (:text args))
                {:ok true  :value {:bytes-written (count (:text args))}}
                {:ok false :error {:code :fs/write-error :message "missing path or text"}})
    {:ok false :error {:code :capability/not-found
                       :message (str "Unknown: " capability)}}))

(deftest fs-read-round-trip
  (testing "valid fs/read call + ok result"
    (let [call   {:capability :fs/read :args {:path "/tmp/x.txt"}}
          result (mock-invoke call)]
      (is (schema/valid? :test/ipc-call call))
      (is (schema/valid? :test/ipc-result result))
      (is (:ok result))
      (is (= "hello fuxx" (get-in result [:value :text]))))))

(deftest fs-read-missing-path
  (testing "fs/read without path returns error result"
    (let [result (mock-invoke {:capability :fs/read :args {}})]
      (is (schema/valid? :test/ipc-result result))
      (is (not (:ok result)))
      (is (= :fs/read-error (get-in result [:error :code]))))))

(deftest fs-write-round-trip
  (testing "valid fs/write call + ok result"
    (let [call   {:capability :fs/write :args {:path "/tmp/x.txt" :text "hello"}}
          result (mock-invoke call)]
      (is (schema/valid? :test/ipc-call call))
      (is (schema/valid? :test/ipc-result result))
      (is (:ok result))
      (is (= 5 (get-in result [:value :bytes-written]))))))

(deftest unknown-capability
  (testing "unknown capability returns not-found error"
    (let [result (mock-invoke {:capability :unicorn/fly :args {}})]
      (is (schema/valid? :test/ipc-result result))
      (is (not (:ok result)))
      (is (= :capability/not-found (get-in result [:error :code]))))))
