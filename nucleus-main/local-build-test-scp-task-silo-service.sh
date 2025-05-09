#!/bin/bash

rm -rf ~/.m2/repository
mvn clean install -U
mvn -B clean install -pl shared-lib
mvn -B clean test -pl task-silo-service
mvn -B package -pl task-silo-service -DskipTests
scp -o StrictHostKeyChecking=no -i ~/task-silo-ec2-test-key-pair.pem ~/algobrewery/nucleus/task-silo-service/target/task-silo-service-0.0.1-SNAPSHOT.jar ec2-user@ec2-3-84-14-171.compute-1.amazonaws.com:/home/ec2-user/task-silo-service/app.jar