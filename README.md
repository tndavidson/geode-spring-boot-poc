# Geode Spring Boot POC

This project is a Proof of Concept (POC) demonstrating the integration of **Spring Boot** with **Apache Geode** (using Spring Data Geode). It provides a simple RESTful service to manage `Customer`, `Account`, and `Transaction` data, which is persisted in an Apache Geode cache.

## Features

- **Spring Data Geode Integration**: Configures a Spring Boot application as an Apache Geode `ClientCache` application.
- **RESTful API**: Provides endpoints for CRUD operations on Customer, Account, and Transaction entities.
- **Continuous Query**: Automatically recalculates account balances whenever a new transaction is processed.
- **Server-Side Function Execution**: Demonstrates executing custom logic on Geode servers for efficient data processing (e.g., retrieving deposit history across multiple accounts).
- **Entity-Based Regions**: Maps domain models to Geode regions using annotations, simplifying data access and repository configuration.
- **PDX Serialization**: Configures reflection-based PDX serialization for efficient data handling in Geode.
- **Behavior-Driven Development (BDD)**: Uses Cucumber for integration testing of the REST API.

## Project Structure

The project is organized into two modules:

- **`geocode-spring-boot-poc-models`**: Contains the shared domain models (e.g., `Customer`) and server-side function implementations. These are built as their own jar, for deploying to Geode.
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
- A running Apache Geode cluster (the current configuration is set up as a `ClientCache`).

### Running Apache Geode

Download and install Apache Geode. Download instructions can be found here: https://github.com/apache/geode?tab=readme-ov-file

From a command line, start the `gfsh` shell. These commands will get you up and running quickly:

```bash
$ gfsh
gfsh> start locator
gfsh> start server
gfsh> create region --name=customer --type=REPLICATE
gfsh> create region --name=account --type=REPLICATE
gfsh> create region --name=transaction --type=REPLICATE
```

To enable OQL queries against the domain objects and to register server-side functions, you will need to build the project and deploy the models jar to the Geode cluster.

From project root:

```bash
./gradlew build
```

```bash
gfsh> deploy --jar="<path-to-project-root>/geocode-spring-boot-poc-models/build/libs/geocode-spring-boot-poc-models-0.0.1-SNAPSHOT.jar"
```

Once deployed, the `DEPOSIT_HISTORY_SERVER_SIDE_FUNCTION` will be available for execution.

To confirm this setup worked correctly, run these commands (your Member name will be different in the output)

```bash
gfsh>list regions
List of regions
---------------
account
customer
transaction

gfsh>list functions
     Member      | Function
---------------- | ------------------------------------
yawn-helpful-can | DEPOSIT_HISTORY_SERVER_SIDE_FUNCTION
```
### Running the Application

To start the server:

```bash
./gradlew :geocode-spring-boot-poc-server:bootRun
```

The server will start on port `8081` (as configured in `application.yml`). Navigating your browser to http://localhost:8081/swagger-ui/index.html will allow you to interact with the APIs using OAS 3.0.

## REST API Endpoints

### Customer API

The `CustomerRestController` provides the following endpoints under the `/customer` base path:

| Method | Endpoint                                         | Description                                                                                              |
|--------|--------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| `GET`  | `/customer`                                      | Retrieve all customers from the Geode cache. Optional query parameter `lastName` to filter by last name. |
| `GET`  | `/customer/{id}`                                 | Retrieve a specific customer by ID.                                                                      |
| `POST` | `/customer`                                      | Create a new customer.                                                                                   |
| `POST` | `/customer/generate-fake-data/{numberOfRecords}` | Generate and save a specified number of fake customers.                                                  |
| `GET`  | `/customer/{id}/deposit-history-all-accounts`    | Retrieve deposit history across all accounts for a specific customer using a server-side Geode function. |

### Account and Transaction API

The `AccountRestController` provides the following endpoints:

| Method | Endpoint                     | Description                                            |
|--------|------------------------------|--------------------------------------------------------|
| `GET`  | `/account/{accountId}`       | Retrieve a specific account by ID.                     |
| `POST` | `/account`                   | Create a new account.                                  |
| `GET`  | `/transaction/{transactionId}`| Retrieve a specific transaction by ID.                 |
| `POST` | `/transaction`               | Create a new transaction.                              |

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
