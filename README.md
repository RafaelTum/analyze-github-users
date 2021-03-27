## Analyze Github Users

The project goal is to retrieve Github users data and store the filtered ones to database.
In the project are demonstrated the use of current technologies like Spring Boot, Spring WebFlux, 
Docker, OpenApi v3, MongoDB with Testcontainers for integration tests.

## Project Features
Below are listed the project key features:
- Retrieving Github Users data from the public api
- Filtering and storing the data to MongoDB
- Search stored Github users by various criteria
- Authenticate with username/password
- Authorization with JWT token

## Get Started
In order to get started with the project make sure to have installed Maven 3+, JDK 8+, MongoDB and Docker.

## Build Project
Run maven clean and install goals to build all necessary artifacts.
Execute the following command in the project root directory:

`mvn clean install`

## To create and run Docker

`docker-compose up -d`

## To Start Server locally without Docker:

`mvn spring-boot:run`

## To use REST Api

`http://localhost:8090/api/v1/**`

##  Docs 
Docs are available by this url:
`http://localhost:8090/docs/swagger-ui`