(ns electron-cljs.core
  (:require cljs.nodejs :as node))

(def electron (node/require "electron"))
