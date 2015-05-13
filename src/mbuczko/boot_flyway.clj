(ns mbuczko.boot-flyway
  {:boot/export-tasks true}
  (:require
   [boot.pod  :as pod]
   [boot.core :as core]
   [boot.util :as util]))

(def fly-deps '[[org.flywaydb/flyway-core "3.1"]])



(core/deftask flyway
  "Apply/rollback flyway migrations"
  [d driver    DRIVER     str  "database driver"
   j url       URL        str  "jdbc url"
   u user      USER       str  "user to connect with"
   p password  PASS       str  "password to connect with"
   c clean                bool "Drop all objects in the configured schemas."
   i info                 bool "Prints the details and status information about all the migrations"
   v validate             bool "Validates the applied migrations against the available ones"
   b baseline             bool "Baselines an existing database, excluding all migrations upto and including baselineVersion"
   r repair               bool "Repair the metadata table"
   g generate  MIGRATION  str  "name of generated migration."
   o options   OPTIONS    edn  "additional flyway options"]
  
  (let [worker (pod/make-pod (update-in (core/get-env) [:dependencies] into fly-deps))
        dataset {:driver driver
                 :url url
                 :user user
                 :password password}
        config (merge options dataset)]

    (if generate
      (let [curr (.format (java.text.SimpleDateFormat. "yyyyMMddhhmmss") (java.util.Date.))
            name (str "db/migration/v" curr "__" generate ".sql")]
        (spit name "-- migration to be applied\n\n")
        (util/info "Created %s\n" name))

      (if-not (and driver url)
        (util/info "No driver or url set\n")
        (pod/with-eval-in worker
          (require '[mbuczko.flyway :as flyway])
          
          (let [fw (flyway/flyway ~config)]
            (doseq [[command _] ~*opts*]
              (case command
                :clean    (flyway/clean fw)
                :info     (flyway/info  fw)
                :validate (flyway/validate fw)
                :baseline (flyway/baseline fw)
                :repair   (flyway/repair fw)
                "default"))))))))
