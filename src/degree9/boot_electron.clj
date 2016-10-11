(ns degree9.boot-electron
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.tmpdir :as tmpd]
            [boot.util :as util]
            [boot.task.built-in :as tasks]
            [clojure.java.io :as io]
            [cheshire.core :refer :all]
            [degree9.boot-exec :as exec]
            [degree9.boot-npm :as npm]
            [adzerk.boot-reload :refer [reload]]
            ))

(boot/deftask electron
  "Generate edn/property files for electron."
  [n name      VAL str  "Name of electron app."
   v version   VAL str  "Version of electron app."
   e edn       VAL str  "Electron main edn name."
   d dev           bool "Set electron-cljs dev flag and reload :ws-host."
   i init-fn   VAL sym  "Electron init function."]
   (let [name      (:name *opts* "Electron")
         version   (:version *opts* "0.1.0")
         edn       (:edn *opts* "electron")
         init      (:init-fn *opts* 'electron-cljs.main/init)
         main      (symbol (namespace init))
         dev       (:dev *opts* false)
         propstr   (generate-string
                      {:name name
                       :version version
                       :main (str edn ".js")})
         ednstr    {:require  [main]
                    :init-fns [init]
                    :compiler-options {:target :nodejs
                                       :closure-defines {'electron-cljs.main/dev? dev}}}
         tmp       (boot/tmp-dir!)
         tmp-path  (.getAbsolutePath tmp)
         ednf      (io/file tmp (str edn ".cljs.edn"))]
      (when dev
        (boot/task-options! reload #(assoc-in % [:ws-host] "localhost")))
     (comp
       (exec/properties :directory tmp-path :file "package.json" :contents propstr)
       (boot/with-pre-wrap fileset
         (doto ednf (spit ednstr))
         (-> fileset (boot/add-resource tmp) boot/commit!))
       )))
