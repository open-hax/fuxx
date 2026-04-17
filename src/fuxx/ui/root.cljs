(ns fuxx.ui.root)

;; Skeleton root view. Replace with real uxx/Helix component tree.
(defn root-view
  [{:keys [app route]}]
  [:div.fuxx-root
   [:header (:app/title app)]
   [:main
    [:pre (pr-str route)]]])
