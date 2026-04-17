(ns fuxx.app)

(defmacro defapp
  "Declare an application manifest."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :app} ~opts)))

(defmacro defview
  "Declare a view — a named, routable UI surface."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :view} ~opts)))

(defmacro defroute
  "Declare a route binding a path pattern to a view and query."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :route} ~opts)))

(defmacro defcommand
  "Declare a named command with typed args and a run expression."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :command} ~opts)))

(defmacro defquery
  "Declare a named query that derives data for a view."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :query} ~opts)))

(defmacro defeffect
  "Declare a named effect that calls a host capability."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :effect} ~opts)))

(defmacro defcapability
  "Declare a named host capability with args and ret schemas."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :capability} ~opts)))

(defmacro defplugin
  "Declare a SCI-backed plugin with an explicit capability grant."
  [sym opts]
  `(def ~sym
     (merge {:fuxx/kind :plugin} ~opts)))
