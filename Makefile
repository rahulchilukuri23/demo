# Project configuration
APP_NAME       := ev-management
VERSION        := 1.0.0
JAR_FILE       := target/$(APP_NAME)-$(VERSION).jar
IMAGE_NAME     := $(APP_NAME):$(VERSION)

# Maven commands
MVN            := mvn
MVN_BUILD      := $(MVN) clean package -DskipTests

# Docker commands
DOCKER         := docker
DOCKER_BUILD   := $(DOCKER) build -t $(IMAGE_NAME) .

# Docker Compose
DC             := docker-compose
COMPOSE_START  := docker-compose-start.yml
COMPOSE_APP    := docker-compose-app.yml

# Network (if needed manually)
NETWORK_NAME   := ev_management_network

.PHONY: all prerequisites build docker run logs down clean

## Full pipeline: start infra, build app, build image
all: prerequisites build docker

## Start supporting services (if any)
prerequisites:
	$(DC) -f $(COMPOSE_START) up -d

## Build the JAR
build:
	$(MVN_BUILD)

## Build Docker image
docker: build
	$(DOCKER_BUILD)

## Start the main application (depends on image)
run: docker
	$(DC) -f $(COMPOSE_APP) up -d

## View logs for the main application
logs:
	$(DC) -f $(COMPOSE_APP) logs -f

## Stop and remove app containers
down:
	$(DC) -f $(COMPOSE_APP) down

## Clean build artifacts and Docker images
clean:
	$(MVN) clean
	$(DOCKER) rmi -f $(IMAGE_NAME) || true
