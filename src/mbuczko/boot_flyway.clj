(ns mbuczko.boot-flyway
  {:boot/export-tasks true}
  (:require
   [clojure.java.io :as io]
   [boot.pod        :as pod]
   [boot.core       :as core]
   [boot.util       :as util]))

(def ^:private fly-deps '[[org.flywaydb/flyway-core "3.2.1"]])

(def ^:private default-location "db/migrations")

(defn find-migrations-dir
  "Looks for migraton location within resource classpath dirs"
  [location]
  (first
   (let [dirs (apply concat (vals (select-keys (core/get-env) [:resource-paths])))]
     (filter #(.exists %) (map #(io/file (str % "/" location)) dirs)))))

(core/deftask flyway
  "Apply/rollback flyway migrations"
  [d driver   DRIVER     str  "database driver"
   j url      URL        str  "jdbc url"
   u user     USER       str  "user to connect with"
   p password PASS       str  "password to connect with"
   c clean               bool "Drop all objects in the configured schemas."
   i info                bool "Prints the details and status information about all the migrations"
   v validate            bool "Validates the applied migrations against the available ones"
   m migrate             bool "Migrates pending migrations"
   b baseline            bool "Baselines an existing database, excluding all migrations upto and including baselineVersion"
   r repair              bool "Repair the metadata table"
   g generate MIGRATION  str  "name of generated migration."
   o options  OPT=VAL{kw str} "additional flyway options"]
  
  (let [worker (pod/make-pod (update-in (core/get-env) [:dependencies] into fly-deps))
        dataset {:driver driver
                 :url url
                 :user user
                 :password password}
        config (merge {:locations [default-location]} options dataset)
        locations (:locations config)]

    (if generate
      (if-let [dir (find-migrations-dir (first locations))]
        (let [curr (.format (java.text.SimpleDateFormat. "yyyyMMddhhmmss") (java.util.Date.))
              name (str (.getPath dir) "/V" curr "__" generate ".sql")]
          (spit name "-- migration to be applied\n\n")
          (util/info "Created %s\n" name)))

      (if-not (and driver url)
        (util/fail "No driver or url set\n")
        (pod/with-eval-in worker
          (require '[mbuczko.flyway :as flyway])
          
          (let [fw (flyway/flyway ~config)]
            (doseq [[command _] ~*opts*]
              (case command
                :clean    (flyway/clean fw)
                :info     (flyway/info  fw)
                :migrate  (flyway/migrate fw)
                :validate (flyway/validate fw)
                :baseline (flyway/baseline fw)
                :repair   (flyway/repair fw)
                "default"))))))))
