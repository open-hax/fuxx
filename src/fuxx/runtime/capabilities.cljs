(ns fuxx.runtime.capabilities
  (:require [fuxx.schema :as schema]
            [fuxx.runtime.state :as state]))

(defn register-capability!
  "Register a capability handler fn under keyword k."
  [k handler]
  (state/swap-runtime! assoc-in [:capabilities k] handler)
  k)

(defn invoke!
  "Validate and invoke a capability call map."
  [{:keys [capability] :as call}]
  (schema/assert-valid! :fuxx.capability/call call)
  (if-let [handler (get-in (state/snapshot) [:capabilities capability])]
    (handler call)
    {:ok    false
     :error {:code    :capability/not-found
             :message (str "Capability not registered: " capability)}}))
