(set-env!
 :dependencies  '[[org.clojure/clojure                 "1.8.0" :scope "provided"]
                  [adzerk/bootlaces                    "0.1.13" :scope "test"]
                  [adzerk/boot-reload                  "0.4.12" :scope "provided"]
                  [boot/core                           "2.6.0"]
                  [cheshire                            "5.6.3"]
                  [degree9/boot-semver                 "1.2.0" :scope "test"]
                  [degree9/boot-npm                    "0.2.0"]
                  [degree9/boot-exec                   "0.5.0-SNAPSHOT"]]
 :resource-paths   #{"src"})

(require
  '[adzerk.bootlaces :refer :all]
  '[boot-semver.core :refer :all]
  '[degree9.boot-electron :refer :all])

(task-options!
  pom {:project 'degree9/boot-electron
       :version (get-version)
       :description "Compile cljs app to electron."
       :url         "https://github.com/degree9/boot-electron"
       :scm         {:url "https://github.com/degree9/boot-electron"}})

(deftask develop
  "Build boot-electron for development."
  []
  (comp
   (watch)
   (version :no-update true
            :minor 'inc
            :patch 'zero
            :pre-release 'snapshot)
   (target  :dir #{"target"})
   (build-jar)))

(deftask deploy
  "Build boot-electron and deploy to clojars."
  []
  (comp
   (version)
   (target  :dir #{"target"})
   (build-jar)
   (push-release)))
