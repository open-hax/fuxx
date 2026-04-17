(ns fuxx.runtime.render
  (:require [fuxx.runtime.state :as state]
            [fuxx.adapters.uxx :as uxx]))

(defn frame
  "Produce the current render frame from runtime state."
  []
  (let [{:keys [app route view]} (state/snapshot)]
    (uxx/render-root {:app app :route route :view view})))
