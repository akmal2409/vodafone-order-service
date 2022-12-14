# vodafone-order-service
Tech Assignment 2.1

# Architecture Clarification (User management)
In the assignment I was requested to check if the user email exists by calling an external API. However, I would not do it myself also in real life unless I am forced too. The reason is simple, that increases the domain coupling and lowers the resilience of the service. Furthermore, it will make the e2e tests much harder because this service will have yet another dependency.
Therefore, for a POC I decided to preload users from the API upon the InMemoryUserRepository bean and storing it in a map for faster lookups. I know its not an elegant solution but in real life I would denormalize the data and bring it to the boundaries of the Order service via async events (probably would use Kafka).

# Quick start (One touch set up with docker compose)
    mvn install
That will  install required dependencies and generate a jar in the target folder
    
    docker compose up
Will spin up postgres image and build an image of the order service locally.
Database and the user will be generated by create_dev_db.sh in a scripts folder.

### Error starting postgres container "permission denied"
You might encounter that issue if you are running on linux or max since docker copies over the permissions of the file into the container.
You can fix that by running following:
    
    chmod +x ./scripts/create_dev_db.sh

### Tweaking dev database configuration properties
In docker-compose.yaml you can change the values for the database, however, make sure that
you change both in application-dev.yaml and for the order-service container.

DEV_USER=order_service 

DEV_PASSWORD=order_service 

DEV_USER_DB_NAME=order_service


### Launching only database locally
You can also use docker-compose-dev.yaml that only spins up the postgres instance.
    
    docker compose -f docker-compose-dev.yaml up

## Manual launch
    mvn install

And then use the spring-boot maven plugin to run the application

    mvn spring-boot:run

## Run tests (Both integration and unit)
    mvn test

## Run integration tests (only) and generate openapi.json
    mvn verify

## Package into a jar
    mvn clean package


## OpenAPI
openAPI json file is located under the openapi/ folder
The json endpoint is also exposed in a running service under 
    
    http://host:port/v1/api-docs


