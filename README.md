# Proxy service [![Java CI with Gradle](https://github.com/sekury/proxy-service/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/sekury/proxy-service/actions/workflows/gradle.yml) ![Coverage](.github/badges/jacoco.svg) ![Branches](.github/badges/branches.svg)
Service provides CRUD operations with proxy entity.

There are few approaches used:
1. Spring Web in [master](https://github.com/sekury/proxy-service) branch
2. Spring Data REST in [data-rest](https://github.com/sekury/proxy-service/tree/data-rest) branch
3. Testing with [testcontainers](https://github.com/sekury/proxy-service/tree/testcontainers) branch

## Build & Start with docker

Run `make start`

or manually:
1. Delete image `docker rmi proxy-service`
2. Build app `./gradlew build`
3. Launch docker compose `docker-compose up`

### Disclaimer

Since `./gradlew bootBuildImage` doesn't provide jdk14, I've been forced to use a custom Dockerfile  

## Test with swagger
[Swagger](http://localhost:8080/swagger-ui/index.html#/)