(ns server
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import
    (java.net
      ServerSocket)))


(defn read-socket
  [socket]
  (.readLine (io/reader socket)))


(defn send-socket
  [socket msg]
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))


(defn serve
  [port handler]
  (with-open [server-sock (ServerSocket. port)
              sock (.accept server-sock)]
    (let [msg-in (read-socket sock)
          msg-out (handler msg-in)]
      (send-socket sock msg-out))))


(defn serve-persistent
  [port handler]
  (let [running (atom true)]
    (future
      (with-open [server-sock (ServerSocket. port)]
        (while @running
          (with-open [sock (.accept server-sock)]
            (let [msg-in (read-socket sock)
                  msg-out (handler msg-in)]
              (send-socket sock msg-out))))))
    running))


(defn http-handler
  [msg-in base-dir]
  (let [[_ path version] (str/split msg-in #" ")
        filename (subs path 1)
        file-res (io/file (str base-dir filename))
        response
        (cond
          (empty? filename)
          {:code "200 OK"
           :body (slurp (io/file (str base-dir "index.html")))}

          (.exists file-res)
          {:code "200 OK"
           :body (slurp file-res)}

          :else
          {:code "404 Not Found"
           :body (slurp (io/file (str base-dir "404.html")))})
        header (str version " " (:code response))]
    (println msg-in)
    (str header "\r\n\r\n" (:body response) "\r\n")))


(defn the-server
  [port handler]
  (let [running (atom true)]
    (future
      (with-open [server-sock (ServerSocket. port)]
        (while @running
          (let [sock (.accept server-sock)]
            (.start (Thread. #(let [msg-in (read-socket sock)
                                    msg-out (handler msg-in)]
                                (send-socket sock msg-out)
                                (.close sock))))))))
    running))
