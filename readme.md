## Build and Start
mvn clean install
mvn spring-boot:run -pl springboot-tester-exposition

### avec profil dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev -pl springboot-tester-exposition


## URL
### API

http://localhost:8080/hello
http://localhost:8080/pigs


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


sudo apt-get install inotify-tools
