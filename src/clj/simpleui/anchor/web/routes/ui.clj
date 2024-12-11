(ns simpleui.anchor.web.routes.ui
  (:require
   [simpleui.anchor.web.middleware.exception :as exception]
   [simpleui.anchor.web.middleware.formats :as formats]
   [simpleui.anchor.web.views.home :as home]
   [simpleui.anchor.web.views.model :as model]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]))

(defn route-data [opts]
  (merge
   opts
   {:muuntaja   formats/instance
    :middleware
    [;; Default middleware for ui
    ;; query-params & form-params
      parameters/parameters-middleware
      ;; encoding response body
      muuntaja/format-response-middleware
      ;; exception handling
      exception/wrap-exception]}))

(derive :reitit.routes/ui :reitit/routes)

(defmethod ig/init-key :reitit.routes/ui
  [_ opts]
  [["" (route-data opts) (home/ui-routes opts)]
   ["/model" (route-data opts) (model/ui-routes opts)]])
