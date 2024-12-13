(ns simpleui.anchor.web.nashorn
  (:import
    jdk.nashorn.api.scripting.NashornScriptEngineFactory))

(def factory (NashornScriptEngineFactory.))

(defn eval-js [s m]
  (let [engine (.getScriptEngine factory)]
    (doseq [[k v] m]
      (.put engine k v))
    (.eval engine s)
    ;; todo
    ))
