IMAGE = proxy-service

build:
	./gradlew build
	docker build -t ${IMAGE} .

start: build
	docker-compose up

.PHONY: build