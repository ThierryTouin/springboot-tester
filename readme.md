## Build and Start
mvn clean install
mvn spring-boot:run -pl springboot-tester-exposition


## URL
### API

http://localhost:8080/hello


### Actuator
- Root : http://localhost:8080/actuator
- Loggers : http://localhost:8080/actuator/loggers
- Détail santé : http://localhost:8080/actuator/health
- Autres : http://localhost:8080/actuator/env, etc.