(ns degree9.boot-electron
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [clojure.java.io :as io]
            [cheshire.core :refer :all]
            [degree9.boot-exec :as exec]))

(boot/deftask electron
  "Generate edn/property files for electron."
  [n name      VAL str  "Name of electron app."
   v version   VAL str  "Version of electron app."
   e edn       VAL str  "Electron main edn name."
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
                   :compiler-options {:target :nodejs}}
        tmp       (boot/tmp-dir!)
        tmp-path  (.getAbsolutePath tmp)
        ednf      (io/file tmp (str edn ".cljs.edn"))]
      (comp
       (exec/properties :directory tmp-path :file "package.json" :contents propstr)
       (boot/with-pre-wrap fileset
         (doto ednf (spit ednstr))
         (-> fileset (boot/add-resource tmp) boot/commit!)))))
