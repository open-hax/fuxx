(ns fuxx.adapters.uxx
  ;; TODO: (:require [open-hax.uxx :as uxx])
  ;; Wire real uxx Helix imports once dependency is established.
  )

(defn render-root
  "Hand off a render frame to the uxx Helix layer.
   Returns a descriptor map until real uxx is wired in."
  [{:keys [app route view]}]
  {:renderer :uxx/helix
   :app      app
   :route    route
   :view     view})
