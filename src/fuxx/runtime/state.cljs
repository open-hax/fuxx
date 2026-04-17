(ns fuxx.runtime.state)

(defonce !runtime
  (atom {:app          nil
         :route        nil
         :view         nil
         :dispatch-log []
         :plugins      {}
         :capabilities {}
         :renderer     {:kind :uxx/helix}}))

(defn snapshot [] @!runtime)

(defn swap-runtime! [f & args]
  (apply swap! !runtime f args))
