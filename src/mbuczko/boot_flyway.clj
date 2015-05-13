(ns mbuczko.boot-flyway
  {:boot/export-tasks true}
  (:require
   [mbuczko.flyway :as flyway]
   [boot.pod       :as pod]
   [boot.core      :as core]
   [boot.util      :as util]))

(def ^:private fly-deps '[[org.flywaydb/flyway-core "3.1"]])

(core/deftask flyway
  "Apply/rollback flyway migrations"
  [d database  DATABASE   str  "database jdbc url"
   g generate  MIGRATION  str  "name of generated migration."
   m migrate              bool "Run all the migrations not applied so far."
   r rollback             int  "number of migrations to be immediately rolled back."
   l list-unapplied       bool "List all migrations to be applied."
   a list-applied         bool "List migration already applied"]
  
  (let [worker  (pod/make-pod (update-in (core/get-env) [:dependencies] into fly-deps))
        command (if rollback :rollback (if migrate :migrate))]

    (if generate
      (let [curr (.format (java.text.SimpleDateFormat. "yyyyMMddhhmmss") (java.util.Date.))
            name (str "migrations/" curr "-" generate)]
        (spit (str name ".up.sql") "-- migration to be applied\n\n")
        (spit (str name ".down.sql") "-- rolling back receipe\n\n")

        (util/info "Created %s\n" name)))

    (if (or list-unapplied command)
      (if-not database
        (util/info "No database set\n")
        (pod/with-eval-in worker
          


          )))))
