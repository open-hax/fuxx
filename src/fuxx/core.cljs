(ns fuxx.core
  (:require [fuxx.contracts :as contracts]
            [fuxx.runtime.state :as state]))

(defn boot!
  "Initialise the fuxx runtime with an app manifest."
  [app]
  (contracts/bootstrap!)
  (state/swap-runtime! assoc :app app)
  {:ok true :app (:app/id app)})
