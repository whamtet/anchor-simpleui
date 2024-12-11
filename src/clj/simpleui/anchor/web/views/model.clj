(ns simpleui.anchor.web.views.model
  (:require
    [simpleui.anchor.i18n :refer [i18n]]
    [simpleui.anchor.web.controllers.model :as model]
    [simpleui.anchor.web.htmx :refer [defcomponent page-htmx]]
    [simpleui.anchor.web.views.components :as components]
    [simpleui.anchor.web.views.dashboard :as dashboard]
    [simpleui.anchor.web.views.lang :as lang]
    [simpleui.anchor.util :refer [defcss]]
    [simpleui.core :as simpleui]
    [simpleui.response :as response]))

(defcomponent ^:endpoint model-rw [req]
  [:form {:hx-post "model-ro"}
   [:textarea {:class "border rounded-md p-2 w-3/4"
               :rows 12
               :name "model"}
    (model/get-model-plain)]
   (components/submit (i18n "Save"))])

(defcomponent ^:endpoint model-ro [req model]
  model-rw
  (when (simpleui/post? req)
    (model/set-model model))
  (let [{:keys [assignment? lines]} (model/get-model)]
    [:div {:hx-target "this"}
     [:div.my-2
      (for [[[lhs equals] & rhs] lines]
        [:div
         [:span.text-clj-green-light lhs]
         equals
         (for [[variable rest] rhs]
           (list
            (if (assignment? variable)
              [:span.text-clj-green-light variable]
              [:span.text-clj-blue-light variable])
            rest))])]
     [:span {:hx-get "model-rw"}
      (components/button (i18n "Edit"))]]))

(defcomponent model [req]
  [:div.min-h-screen.p-2 {:_ "on click add .hidden to .drop"}
   [:a.absolute.top-2.left-2 {:href "/"}
    [:img.w-10.opacity-70 {:src "/anchor.svg"}]]
   ;; dropdown
   (dashboard/main-dropdown)
   [:div.mt-14
    [:h3.text-2xl (i18n "Financial Valuation Model")]
    (model-ro req)]
   (lang/lang-dropup req)])

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
