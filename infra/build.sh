#!bash

cd ../adminservice
./mvnw clean package -Dmaven.test.skip=true

cd ../courierservice
./mvnw clean package -Dmaven.test.skip=true