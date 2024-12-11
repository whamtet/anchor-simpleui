(ns simpleui.anchor.web.controllers.model
  (:require
    [clojure.java.io :as io]))

(def model (atom (-> "default_model.js" io/resource slurp)))

(defn- parse-line [^String s]
  (loop [s s
         done ()]
    (if-let [word (re-find #"[a-zA-Z_]\w*" s)]
      (let [i1 (.indexOf s word)
            i2 (+ i1 (count word))]
        (recur
          (.substring s i2)
          (conj done (.substring s 0 i1) (.substring s i1 i2))))
      (->> (conj done s) reverse rest (partition-all 2)))))

(defn- get-model* []
  (map parse-line (.split @model "\n")))

(defn get-model []
  (let [lines (get-model*)]
    {:assignment? (set (map ffirst lines))
     :lines lines}))

(defn get-model-plain []
  @model)

(defn set-model [^String s] (reset! model (.trim s)))
