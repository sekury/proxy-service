# Proxy service
Service provides CRUD operations with proxy entity.

There are two approaches used:
1. Spring Web in [master](https://github.com/sekury/proxy-service) branch
2. Spring Data REST in [data-rest](https://github.com/sekury/proxy-service/tree/data-rest) branch

## Build & Start with docker

1. Delete image `docker rmi proxy-service`
2. Build image `./gradlew build`
3. Launch docker compose `docker-compose up`

### Disclaimer

Since `./gradlew bootBuildImage` doesn't provide jdk14, I've been forced to use custom .Dockerfile  

## Test with swagger
[Swagger](http://localhost:8080/swagger-ui/index.html#/)