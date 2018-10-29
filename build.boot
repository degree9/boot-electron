(set-env!
 :dependencies  '[[org.clojure/clojure                 "1.9.0"]
                  [boot/core                           "2.7.2"]
                  [cheshire                            "5.8.1"]
                  [degree9/boot-semver                 "1.7.0" :scope "test"]
                  [degree9/boot-exec                   "1.0.0"]]
 :resource-paths   #{"src"})

(require
  '[degree9.boot-semver :refer :all]
  '[degree9.boot-electron :refer :all])

(task-options!
  pom {:project 'degree9/boot-electron
       :description "Compile cljs app to electron."
       :url         "https://github.com/degree9/boot-electron"
       :scm         {:url "https://github.com/degree9/boot-electron"}})

(deftask develop
  "Build boot-electron for development."
  []
  (comp
   (version :develop true
            :minor 'inc
            :patch 'zero
            :pre-release 'snapshot)
   (watch)
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
