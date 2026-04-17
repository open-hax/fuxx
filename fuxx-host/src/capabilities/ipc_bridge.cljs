;; fuxx.host.ipc-bridge
;; CLJS side of the IPC channel.
;; Call (invoke! call-map) from fuxx.runtime.capabilities.

(ns fuxx.host.ipc-bridge)

(defn invoke!
  "Send a capability call map to the Rust host and return a Promise<result-map>."
  [call]
  (js/Promise.
   (fn [resolve reject]
     (if-let [ipc (.-__fuxx_ipc js/window)]
       (-> (ipc (js/JSON.stringify (clj->js call)))
           (.then #(resolve (js->clj (js/JSON.parse %) :keywordize-keys true)))
           (.catch #(reject %)))
       (reject (js/Error. "fuxx: __fuxx_ipc not available (not running in host shell)"))))))
