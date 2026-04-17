(ns fuxx.schema
  (:require [malli.core :as m]
            [clojure.edn :as edn]))

;; ── Registry ────────────────────────────────────────────────────────────────

(defonce !registry (atom {}))

(defn load-contracts!
  "Read an EDN map of {k {:schema ...}} into the registry."
  [path]
  (let [data (-> path slurp edn/read-string)]
    (swap! !registry merge data)
    @!registry))

(defn register!
  [k spec]
  (swap! !registry assoc k spec)
  k)

(defn registry [] @!registry)

;; ── Lookup ──────────────────────────────────────────────────────────────────

(defn schema
  [k]
  (or (:schema (get @!registry k))
      (throw (ex-info "Unknown schema" {:key k}))))

;; ── Validation ──────────────────────────────────────────────────────────────

(defn valid?
  [k value]
  (m/validate (schema k) value))

(defn explain
  [k value]
  (m/explain (schema k) value))

(defn validator
  [k]
  (m/validator (schema k)))

(defn assert-valid!
  "Throw ex-info with explain data when value does not satisfy schema k."
  [k value]
  (when-not (valid? k value)
    (throw (ex-info "Contract validation failed"
                    {:contract k
                     :value    value
                     :explain  (explain k value)})))
  value)
