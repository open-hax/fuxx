(ns fuxx.contracts
  (:require [fuxx.schema :as schema]))

(def contract-paths
  ["contracts/envelopes.edn"
   "contracts/capabilities.edn"
   "contracts/plugins.edn"])

(defn bootstrap!
  "Load all EDN contract files into the schema registry."
  []
  (doseq [path contract-paths]
    (schema/load-contracts! path))
  (schema/registry))
