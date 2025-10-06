## Build & Start
Before start, you must build.

Use `_cmd.sh` script

## URL
### API

http://localhost:8080/hello
http://localhost:8080/pigs

### Swagger
http://localhost:8080/swagger-ui/index.html


### Actuator
- Root : http://localhost:8080/actuator
- Loggers : http://localhost:8080/actuator/loggers
- Détail santé : http://localhost:8080/actuator/health
- Autres : http://localhost:8080/actuator/env, etc.
- Beans : http://localhost:8080/actuator/beans


## Pour forcer le redémarrage auto a prendre en compte les nouvelles classes, on supprime les jars

find . -type f -name "springboot-tester*.jar" -path "*/target/*" -exec rm -v {} +
rm -rv ~/.m2/repository/com/touin/thierry/sb/test/springboot-tester-*



logging.config=classpath:logback-dev.xml
mvn clean verify

sudo apt-get install inotify-tools








