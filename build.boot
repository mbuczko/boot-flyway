(set-env!
 :source-paths #{"src"}
 :dependencies '[[adzerk/bootlaces "0.1.11" :scope "test"]])

(require '[adzerk.bootlaces :refer :all]
         '[mbuczko.boot-flyway :refer [flyway]])

(def +version+ "0.1.1")

(bootlaces! +version+)

(task-options!
 pom {:project 'mbuczko/boot-flyway
      :version +version+
      :description "Run flyway migrations in boot."
      :url "https://github.com/mbuczko/boot-flyway"
      :scm {:url "https://github.com/mbuczko/boot-flyway"}
      :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})
