(defproject reagent-batteries-included-token-auth "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.7.0-RC2"]
                 [ring-server "0.4.0"]
                 [cljsjs/react "0.13.3-0"]
                 [reagent "0.5.0"]
                 [reagent-forms "0.5.6"]
                 [reagent-utils "0.1.5"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.5"]
                 [cljs-ajax "0.3.13"]
                 [funcool/promesa "0.2.0"]
                 [prone "0.8.2"]
                 [compojure "1.3.4"]
                 [hiccup "1.0.5"]
                 [environ "1.0.0"]
                 [alandipert/storage-atom "1.2.4"]
                 [com.andrewmcveigh/cljs-time "0.3.10"]
                 [org.clojure/clojurescript "1.7.107" :scope "provided"]
                 [secretary "1.2.3"]]

  :plugins [[lein-environ "1.0.0"]
            [lein-asset-minifier "0.2.2"]
            [lein-figwheel "0.3.3"]
            [lein-cljsbuild "1.0.6"]]

  :ring {:handler reagent-batteries-included-token-auth.handler/app
         :uberwar-name "reagent-batteries-included-token-auth.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "reagent-batteries-included-token-auth.jar"

  :main reagent-batteries-included-token-auth.server

  :clean-targets ^{:protect false} [:target-path
                                    [:cljsbuild :builds :app :compiler :output-dir]
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
  :less {:source-paths ["src/less/site" "src/less/site/auth"]
         :target-path "resources/public/css"}

  :profiles {:dev {:repl-options {:init-ns reagent-batteries-included-token-auth.repl}

                   :dependencies [[ring/ring-mock "0.2.0"]
                                  [ring/ring-devel "1.3.2"]
                                  [leiningen-core "2.5.1"]
                                  [lein-figwheel "0.3.3"]
                                  [org.clojure/tools.nrepl "0.2.10"]
                                  [pjstadig/humane-test-output "0.7.0"]]

                   :source-paths ["env/dev/clj"]
                   :plugins [[com.cemerick/clojurescript.test "0.3.3"]
                             [lein-less "1.7.5"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :nrepl-port 7888
                              :css-dirs ["resources/public/css"]
                              :ring-handler reagent-batteries-included-token-auth.handler/app}

                   :env {:dev true}

                   :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                                              :compiler {:main "reagent-batteries-included-token-auth.dev"
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
