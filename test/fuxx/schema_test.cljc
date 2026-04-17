(ns fuxx.schema-test
  (:require [clojure.test :refer [deftest is testing run-tests]]
            [fuxx.schema :as schema]
            [malli.core :as m]))

;; ── Inline schemas (no EDN file I/O in test env) ──────────────────────

(def envelope-schema
  [:map
   [:id :string]
   [:kind [:enum :cmd :evt :qry :res :err]]
   [:name :keyword]
   [:ts inst?]
   [:payload :map]
   [:meta {:optional true} :map]])

(def capability-call-schema
  [:map
   [:capability :keyword]
   [:args :map]
   [:caller {:optional true} :keyword]
   [:request-id {:optional true} :string]])

(def plugin-manifest-schema
  [:map
   [:plugin/id :keyword]
   [:plugin/name :string]
   [:plugin/version :string]
   [:plugin/capabilities [:vector :keyword]]])

;; ── Register for tests ────────────────────────────────────────────

(schema/register! :test/envelope        {:schema envelope-schema})
(schema/register! :test/capability-call {:schema capability-call-schema})
(schema/register! :test/plugin-manifest {:schema plugin-manifest-schema})

;; ── Tests ───────────────────────────────────────────────────────────

(deftest envelope-valid
  (testing "valid cmd envelope passes"
    (is (schema/valid?
         :test/envelope
         {:id      "abc-1"
          :kind    :cmd
          :name    :route/navigate
          :ts      (java.util.Date.)
          :payload {:to [:view/home {}]}}))

  (testing "missing :id fails"
    (is (not (schema/valid?
              :test/envelope
              {:kind :cmd :name :noop :ts (java.util.Date.) :payload {}}))))))

(deftest capability-call-valid
  (testing "valid fs/read call"
    (is (schema/valid?
         :test/capability-call
         {:capability :fs/read
          :args       {:path "/tmp/test.txt"}})))

  (testing "missing :args fails"
    (is (not (schema/valid?
              :test/capability-call
              {:capability :fs/read})))))

(deftest plugin-manifest-valid
  (testing "valid plugin manifest"
    (is (schema/valid?
         :test/plugin-manifest
         {:plugin/id           :notes
          :plugin/name         "Notes"
          :plugin/version      "0.1.0"
          :plugin/capabilities [:fs/read :fs/write]})))

  (testing "missing :plugin/version fails"
    (is (not (schema/valid?
              :test/plugin-manifest
              {:plugin/id           :notes
               :plugin/name         "Notes"
               :plugin/capabilities [:fs/read]})))))

(deftest assert-valid-throws
  (testing "assert-valid! throws on bad data"
    (is (thrown? #?(:clj clojure.lang.ExceptionInfo :cljs cljs.core/ExceptionInfo)
                 (schema/assert-valid!
                  :test/envelope
                  {:kind :bad-kind :payload {}})))))
