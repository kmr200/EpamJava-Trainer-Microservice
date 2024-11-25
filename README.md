## TL;DR
### Build:
```zsh
./gradlew build
```
This will create an uber JAR and a regular JAR
### Run:
```zsh
./gradlew run
```
To run the JAR file:
```zsh
java -jar ./build/libs/GymCrm-1.1.jar
```
### Test:
Command to run all the tests:
```zsh
./gradlew test
```
### Overview:
Trainer-workload-service is a secondary microservice and by default starts up at port: 8081. Microservice\
has no security implementation and is expected to be run behind the main microservice (GymCrm)\
You can import Postman collection using this file: [FILE](./src/test/postman/Collection-microservice.json)
### Controllers:
Microservice implements two controllers (Training and Trainer).\
#### Training Controller has the following endpoints:
Create training: POST /api/v1/trainer-workload/training/ \
Creates training along with the trainer if it is not present.
#### Trainer Controller has the following endpoints:
Get trainings summary: GET /api/v1/trainer-workload/trainer/{username}/trainings \
returns summary of the trainings after the current date\
\
Update trainer: PUT /api/v1/trainer-workload/trainer/{username}/ \
Updates firstName/lastName/active of the trainer. If the trainer is inactive, all the trainings related to the trainer are deleted
### Repository:
Implements in-memory H2 database with two tables (trainer, training). Some data is preloaded  for testing purposes.
You can check out the script here: [Schema](./src/main/resources/schema.sql), [Data](./src/main/resources/data.sql)