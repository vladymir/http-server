(ns core
  (:require
    [server :as s]))


(defn -main
  [& args]
  (println "Main Invoked")
  (when (= 1 (count args))
    (when-let [config (read-string (first args))]
      (println config)
      (s/the-server (:port config) #(s/http-handler % (:base-dir config))))))
