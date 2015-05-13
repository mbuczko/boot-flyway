# boot-flyway
Flyway migrations with Clojure Boot build tool

This is a simple task for [boot](https://github.com/boot-clj/boot) to generate, apply and inspect database migrations.

    $ boot -h
    Evolve your Database Schema easily and reliably across all your instances.

    Options:
      -h, --help                Print this help info.
      -d, --database DATABASE   Set database jdbc url to DATABASE.
      -g, --generate MIGRATION  Set name of generated migration to MIGRATION.
      -m, --migrate             Run all the migrations not applied so far.
      -r, --rollback            Increase number of migrations to be immediately rolled back.
      -l, --list-migrations     List all migrations to be applied.
      

## Examples

To generate brand new migration (up- and down-files located in migrations/ folder):

    boot flyway -g "add-user-table"
    
To list all the migration not applied so far:
    
To simplify those commands you may set task option in build.boot:

    (task-options!
       flyway {:database "jdbc:postgresql://localhost:5432/template1?user=postgres"})
   
and now, you may omit -d ... option from command line.
