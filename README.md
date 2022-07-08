Dependency Report Service API
==============================

A simple api that takes a bill of materials (currently as cycloneDx json) and can be queried for distilled information
about the cyclone.

# Dependencies

* jdk17
* Kotlin
* maven
* postgres (easiest to start with dokr)

# Run local

`./start-local-db`
`./mvnw spring-boot:run`

It will listen on port 4444.

* There is a swagger-ui interface available at http://localhost:4444/swagger-ui/

# Building

1. To build the artifacts just run `./mvnw package` and the jar file will be output under ./target
2. To produce a docker image run `./mvnw docker:build`, you can run the image with `./mvnw docker:run`
