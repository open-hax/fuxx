(ns fuxx.runtime.plugins
  (:require [fuxx.schema :as schema]
            [fuxx.runtime.state :as state]))

;; SCI plugin loading stub.
;; TODO: wire babashka/sci for actual eval with whitelist + capability grants.

(defn load-plugin!
  "Register a plugin manifest and associate its SCI source for later eval."
  [manifest source]
  (schema/assert-valid! :fuxx.plugin/manifest manifest)
  (state/swap-runtime! assoc-in [:plugins (:plugin/id manifest)]
                       {:manifest manifest
                        :source   source
                        :runtime  :sci})
  {:ok true :plugin/id (:plugin/id manifest)})
