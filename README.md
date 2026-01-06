# Geode Spring Boot POC

This project is a Proof of Concept (POC) demonstrating the integration of **Spring Boot** with **Apache Geode** (using Spring Data Geode). It provides a simple RESTful service to manage `Customer` data, which is persisted in an Apache Geode cache.

## Features

- **Spring Data Geode Integration**: Configures a Spring Boot application as an Apache Geode `ClientCache` application.
- **RESTful API**: Provides endpoints for CRUD operations on Customer entities.
- **Entity-Defined Regions**: Automatically creates Geode regions based on domain model annotations.
- **PDX Serialization**: Configures reflection-based PDX serialization for efficient data handling in Geode.
- **Behavior-Driven Development (BDD)**: Uses Cucumber for integration testing of the REST API.

## Project Structure

The project is organized into two modules:

- **`geocode-spring-boot-poc-models`**: Contains the shared domain models (e.g., `Customer`). These are built as their own jar, for deploying to Geode.
- **`geocode-spring-boot-poc-server`**: The main Spring Boot application containing the REST controllers, repositories, and Geode configuration.

## Technologies Used

- **Java 17**
- **Spring Boot 3.3.x** (Spring Data Geode compatibility seems to have gone away with newer boot versions)
- **Spring Data Geode**
- **Apache Geode**
- **Lombok**
- **Cucumber** (for testing)
- **Gradle** (Kotlin DSL)

## Getting Started

### Prerequisites

- JDK 17 or higher
- A running Apache Geode cluster if not running in local/embedded mode (the current configuration is set up as a `ClientCache`).

### Running Apache Geode

Download and install Apache Geode. Download instructions can be found here: https://github.com/apache/geode?tab=readme-ov-file

From a command line, start the `gfsh` shell. These commands will get you up and running quickly:

```bash
$ gfsh
gfsh> start locator
gfsh> start server
gfsh> create region --name=customer --type=REPLICATE
```

If you want the ability to run OQL queries in the `gfsh` shell, you will need to build the project and deploy the models jar to the Geode cluster.

From project root:

```bash
./gradlew build
```

```bash
gfsh> deploy --jar="<path-to-project-root>/geocode-spring-boot-poc-models/build/libs/geocode-spring-boot-poc-models-0.0.1-SNAPSHOT.jar"
```

### Running the Application

To start the server:

```bash
./gradlew :geocode-spring-boot-poc-server:bootRun
```

The server will start on port `8081` (as configured in `application.yml`). Navigating your browser to http://localhost:8081/swagger-ui/index.html will allow you to interact with the APIs using OAS 3.0.

## REST API Endpoints

The `CustomerRestController` provides the following endpoints under the `/customer` base path:

| Method | Endpoint                                         | Description                                                                                              |
|--------|--------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| `GET`  | `/customer`                                      | Retrieve all customers from the Geode cache. Optional query parameter `lastName` to filter by last name. |
| `GET`  | `/customer/{id}`                                 | Retrieve a specific customer by ID.                                                                      |
| `POST` | `/customer`                                      | Create a new customer.                                                                                   |
| `POST` | `/customer/generate-fake-data/{numberOfRecords}` | Generate and save a specified number of fake customers.                                                  |

## Configuration

- **`GeodeSpringBootPocApplication`**: Enables GemFire repositories and entity-defined regions.
- **`CacheConfiguration`**: Sets up `ReflectionBasedAutoSerializer` for the `com.github.tndavidson.geodespringbootpoc.model` package to handle PDX serialization.
- **`application.yml`**: Configures the application name, server port, and basic Geode PDX settings.

## Testing

The project uses Cucumber for integration tests. You can find the feature files in `geocode-spring-boot-poc-server/src/test/resources/features`.
The Cucumber tests are configured to run against a mocked Geode environment (see `SpringGeodeTestBase`).
To run the tests:

```bash
./gradlew test
```
