(ns reagent-batteries-included-token-auth.auth.shared-functions
  (:require [ajax.core :refer [GET]]
            [reagent.session :as session]
            [cljs-time.core :as t]
            [cljs-time.coerce :as coerce-t]
            [goog.crypt.base64 :as b64]
            [secretary.core :as secretary :include-macros true]
            [reagent-batteries-included-token-auth.config :refer [endpoints]]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.index :as index]))

;; ==============================
;  LogIn User
(defn login-response-handler [response]
  (swap! ss/auth-creds-ls assoc :id (:id response))
  (swap! ss/auth-creds-ls assoc :token (:token response))
  (swap! ss/auth-creds-ls assoc :refresh-token (:refreshToken response))
  (swap! ss/auth-creds-ls assoc :permissions (:permissions response))
  (swap! ss/auth-creds-ls assoc :username (:username response))
  (swap! ss/auth-creds-ls assoc :time-stamp (coerce-t/to-long (t/date-time (t/now))))
  (reset! ss/flash-message {:kind "success" :text (str "Welcome " (:username response))})
  (if (= @ss/secured-route "")
    (do (swap! ss/nav-state assoc :active-route "/index")
        (session/put! :current-page #'index/index-page)
        (set! (.-location js/window) "#/"))
    (do (session/put! :current-page @ss/secured-route) (reset! ss/secured-route ""))))

(defn login-error-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn auth-header [username password]
  (str "Basic " (b64/encodeString (str username ":" password))))

(defn attempt-login [username password]
  (GET (:auth endpoints) {:headers         {"Authorization" (auth-header username password)}
                          :handler         login-response-handler
                          :error-handler   login-error-handler
                          :response-format :json
                          :keywords?       true
                          :prefix          true}))
