(ns fuxx.runtime.dispatch
  (:require [fuxx.schema :as schema]
            [fuxx.runtime.state :as state]
            [fuxx.runtime.router :as router]
            [fuxx.runtime.capabilities :as caps]))

;; ── Envelope dispatch ───────────────────────────────────────────────────────

(defmulti handle-envelope! (fn [env] (:kind env)))

(defmethod handle-envelope! :cmd
  [{:keys [name payload] :as env}]
  (schema/assert-valid! :fuxx.contract/envelope env)
  (state/swap-runtime! update :dispatch-log conj env)
  (case name
    :route/navigate     (router/navigate! payload)
    :capability/invoke  (caps/invoke! payload)
    {:ok true :unhandled name}))

(defmethod handle-envelope! :evt
  [env]
  (schema/assert-valid! :fuxx.contract/envelope env)
  (state/swap-runtime! update :dispatch-log conj env)
  {:ok true :event (:name env)})

(defmethod handle-envelope! :default
  [env]
  (schema/assert-valid! :fuxx.contract/envelope env)
  (state/swap-runtime! update :dispatch-log conj env)
  {:ok true :pass-through (:name env)})

(defn dispatch!
  "Main entry point. Validate envelope then route to appropriate handler."
  [env]
  (handle-envelope! env))
