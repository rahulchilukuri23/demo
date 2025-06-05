# Project configuration
APP_NAME = ev-management
VERSION = 1.0.0
JAR_FILE = target/$(APP_NAME)-$(VERSION).jar
IMAGE_NAME = $(APP_NAME):$(VERSION)

# Maven commands
MVN = mvn
MVN_BUILD = $(MVN) clean package -DskipTests

# Docker commands
DOCKER = docker
DOCKER_BUILD = $(DOCKER) build -t $(IMAGE_NAME) .

.PHONY: all build test clean docker run

all: build

build:
	$(MVN_BUILD)

test:
	$(MVN) test

clean:
	$(MVN) clean

docker: build
	$(DOCKER_BUILD)