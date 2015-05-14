# boot-flyway
[![Clojars Project](http://clojars.org/mbuczko/boot-flyway/latest-version.svg)](http://clojars.org/mbuczko/boot-flyway)

[Flyway](http://flywaydb.org/) migrations with Clojure Boot build tool

This is a simple task for [boot](https://github.com/boot-clj/boot) to generate, apply and inspect database migrations.
It's mostly a blatant copy of metaphor's [lein-flyway](https://github.com/metaphor/lein-flyway) adjusted to boot.

    $ boot -h
    Evolve your Database Schema easily and reliably across all your instances.

    Options:
      -h, --help                Print this help info.
      -d, --driver DRIVER       Set database driver to DRIVER.
      -j, --url URL             Set jdbc url to URL.
      -u, --user USER           Set user to connect with to USER.
      -p, --password PASS       Set password to connect with to PASS.
      -c, --clean               Drop all objects in the configured schemas.
      -i, --info                Prints the details and status information about all the migrations
      -v, --validate            Validates the applied migrations against the available ones
      -m, --migrate             Migrates pending migrations
      -b, --baseline            Baselines an existing database, excluding all migrations upto and including baselineVersion
      -r, --repair              Repair the metadata table
      -g, --generate MIGRATION  Set name of generated migration to MIGRATION.
      -o, --options OPT=VAL     Conj [OPT VAL] onto additional flyway options
      
Options may contains one of flyway defined ones:

    baseline-version-as-string, baseline-description, locations, table, schemas, sql-migration-prefix, sql-migration-separator, sql-migration-suffix, encoding, placeholders, placeholder-replacement, placeholder-prefix, placeholder-suffix, resolvers, callbacks, target, out-of-order, validate-on-migrate, clean-on-validation-error, baseline-on-migrate
    
Not all have been tested, though :)

One note, ```locations``` is set by default to ```db/migrations``` which means that flyway will be looking for migration files at this directory (within available classpath). Changing this location is as easy as providing ```-o locations=my_dir``` as task parameter. 

As a consequence, also generating new migrations (```-g```) will try to find ```locations``` dir in classpath and place there newly created file.

## Examples

To simplify all commands where driver and url is required, you may set task option in build.boot:

    (task-options!
       flyway  {:driver "org.postgresql.Driver"
                :url "jdbc:postgresql://localhost:5432/template1?user=postgres"})
   
and now, you may omit ```-d``` and ```-j``` option from command line.

To generate brand new migration:

    $ boot flyway -g "add_user_table"
    Created resources/db/migrations/V20150514114110__add_user_table.sql

To clean database:

    $ boot flyway -c

To get an information about applied / pending migrations:

    $ boot flyway -i
    +----------------+---------------------------+---------------------+---------+
    | Version        | Description               | Installed on        | State   |
    +----------------+---------------------------+---------------------+---------+
    | 20150424001323 | add categories table      | 2015-05-14 11:24:11 | Success |
    | 20150512013853 | add user table            | 2015-05-14 11:24:11 | Success |
    | 20150512015102 | add is-confirmed to users | 2015-05-14 11:24:11 | Pending |
    +----------------+---------------------------+---------------------+---------+
    
To set baseline:

    $ boot flyway -b -o baseline-version-as-string=5.2
    

##LICENSE

Copyright © Michał Buczko

Licensed under the EPL.
