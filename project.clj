(defproject reagent-batteries-included-basic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [ring-server "0.4.0"]
                 [cljsjs/react "0.13.1-0"]
                 [reagent "0.5.0"]
                 [reagent-forms "0.5.1"]
                 [reagent-utils "0.1.4"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.4"]
                 [cljs-ajax "0.3.11"]
                 [prone "0.8.2"]
                 [compojure "1.3.3"]
                 [hiccup "1.0.5"]
                 [environ "1.0.0"]
                 [org.clojure/clojurescript "0.0-3291" :scope "provided"]
                 [secretary "1.2.3"]]

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-asset-minifier "0.2.2"]]

  :ring {:handler reagent-batteries-included-basic.handler/app
         :uberwar-name "reagent-batteries-included-basic.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "reagent-batteries-included-basic.jar"

  :main reagent-batteries-included-basic.server

  :clean-targets ^{:protect false} [[:cljsbuild :builds :app :compiler :output-dir]
                                    [:cljsbuild :builds :app :compiler :output-to]]

  :minify-assets
  {:assets
    {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}}}}
  :less {:source-paths ["src/less/site"]
         :target-path "resources/public/css"}

  :profiles {:dev {:repl-options {:init-ns reagent-batteries-included-basic.repl
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[ring-mock "0.1.5"]
                                  [ring/ring-devel "1.3.2"]
                                  [weasel "0.6.0"]
                                  [leiningen-core "2.5.1"]
                                  [lein-figwheel "0.3.3"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.10"]
                                  [pjstadig/humane-test-output "0.7.0"]]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.3.3"]
                             [lein-cljsbuild "1.0.6"]
                             [com.cemerick/clojurescript.test "0.3.2"]
                             [lein-less "1.7.2"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :css-dirs ["resources/public/css"]
                              :ring-handler reagent-batteries-included-basic.handler/app}

                   :env {:dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:main "reagent-batteries-included-basic.dev"
                                                         :source-map true}}
                                        :test {:source-paths ["src/cljs"  "test/cljs"]
                                               :compiler {:output-to "target/test.js"
                                                          :optimizations :whitespace
                                                          :pretty-print true}}}
                               :test-commands {"unit" ["phantomjs" :runner
                                                       "test/vendor/es5-shim.js"
                                                       "test/vendor/es5-sham.js"
                                                       "test/vendor/console-polyfill.js"
                                                       "target/test.js"]}}}

             :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :cljsbuild {:jar true
                                   :builds {:app
                                             {:source-paths ["env/prod/cljs"]
                                              :compiler
                                              {:optimizations :advanced
                                               :pretty-print false}}}}}})
