(ns fuxx.dev
  (:require [fuxx.runtime.state :as state]))

(defn on-reload!
  "Called by shadow-cljs hot-reload after each recompile."
  []
  (js/console.log "[fuxx] hot reload" (clj->js (state/snapshot))))
