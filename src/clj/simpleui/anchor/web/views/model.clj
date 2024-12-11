(ns simpleui.anchor.web.views.model
  (:require
    [simpleui.anchor.i18n :refer [i18n]]
    [simpleui.anchor.web.htmx :refer [defcomponent page-htmx]]
    [simpleui.anchor.web.views.dashboard :as dashboard]
    [simpleui.anchor.web.views.lang :as lang]
    [simpleui.core :as simpleui]
    [simpleui.response :as response]))

(defcomponent model [req]
  (let []
    [:div.min-h-screen.p-2 {:_ "on click add .hidden to .drop"}
     ;; dropdown
     (dashboard/main-dropdown)
     "hi"
     (lang/lang-dropup req)]))

(defn ui-routes [{:keys [query-fn]}]
  (simpleui/make-routes
   ""
   [query-fn]
   (fn [req]
     (if (some->> req :session :id)
       (let [req (assoc req :query-fn query-fn)]
         (page-htmx
          {:css ["/output.css"] :hyperscript? true}
          (model req)))
       (response/redirect "/")))))
