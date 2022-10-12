#!bash

cd ../tokens-ms
./mvnw clean package -Dmaven.test.skip=true

cd ../order-ms
./mvnw clean package -Dmaven.test.skip=true

cd ../identity-ms
./mvnw clean package -Dmaven.test.skip=true
