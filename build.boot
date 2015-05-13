(set-env!
 :source-paths   #{"src"}
 :dependencies '[[org.clojure/clojure "1.6.0" :scope "provided"]
                 [boot/core "2.0.0-rc8" :scope "provided"]
                 [adzerk/bootlaces "0.1.11" :scope "test"]])

(require '[adzerk.bootlaces :refer :all]
         '[mbuczko.boot-flyway :refer [flyway]])

(def +version+ "0.1.0-SNAPSHOT")

(bootlaces! +version+)

(task-options!
 pom {:project 'mbuczko/boot-flyway
      :version +version+
      :description "Run flyway migrations in boot."
      :url "https://github.com/mbuczko/boot-flyway"
      :scm {:url "https://github.com/mbuczko/boot-flyway"}
      :license {"name" "GNU Lesser General Public License"
                "url" "http://www.gnu.org/licenses/lgpl.txt"}})
