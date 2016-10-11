(ns electron-cljs.app
  (:require [electron-cljs.core :as core]
            [goog.object :as obj]))

(def app (obj/get core/electron "app"))
