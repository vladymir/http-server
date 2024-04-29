(ns core
  (:require
    [server :as s]))


(defn -main
  [& args]
  (println "Main Invoked")
  (when (= 1 (count args))
    (when-let [;; config (read-string (first args))
               config-file (-> args first slurp read-string)]
      (println config-file)
      (s/the-server (:port config-file) #(s/http-handler % (:base-dir config-file))))))
