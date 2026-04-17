(ns fuxx.runtime.router
  (:require [fuxx.runtime.state :as state]))

(defn navigate!
  [{:keys [route params]}]
  (state/swap-runtime! assoc :route {:route route :params params}))

(defn current-route []
  (:route (state/snapshot)))
