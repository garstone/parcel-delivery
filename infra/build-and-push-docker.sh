#!bash

cd ../auth-ms
docker build
docker push

cd ../auth-identity
docker build
docker push

cd ../auth-order
docker build
docker push