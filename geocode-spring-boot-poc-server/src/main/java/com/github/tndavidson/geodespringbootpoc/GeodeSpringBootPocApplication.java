package com.github.tndavidson.geodespringbootpoc;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableStatistics;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Entry point for the Geode Spring Boot Proof of Concept application.
 *
 * This class is responsible for initializing and configuring the application,
 * leveraging Apache Geode as a distributed in-memory data grid for caching and data storage.
 * It utilizes Spring Data for GemFire to handle entity mappings and repository functionality.
 *
 * Annotations:
 * - {@code @SpringBootApplication}: Marks this class as the main configuration class and entry point for the Spring Boot application.
 * - {@code @EnableEntityDefinedRegions}: Configures GemFire regions based on the annotated domain model, scanning the specified base package.
 * - {@code @ClientCacheApplication}: Enables an Apache Geode client cache configuration with subscription capabilities.
 * - {@code @EnableGemfireRepositories}: Enables the use of GemFire repositories for data access, specifying the base package for repository scanning.
 * - {@code @EnableStatistics}: Enables collection and reporting of statistics for the Apache Geode client cache.
 * - {@code @EnableContinuousQueries}: Enables the use of Continuous Queries for real-time data event listening.
 * - {@code @EnableGemfireFunctionExecutions}: Enables the execution of GemFire functions, scanning the specified base package for annotated function-related components.
 *
 * The configuration integrates Apache Geode's key features, including caching, repositories, statistics,
 * continuous queries, and function execution, allowing for scalable and efficient data processing.
 */
@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = {Customer.class})
@ClientCacheApplication(subscriptionEnabled = true)
@EnableGemfireRepositories(basePackages = "com.github.tndavidson.geodespringbootpoc.repository")
@EnableStatistics
@EnableContinuousQueries
@EnableGemfireFunctionExecutions(basePackages = "com.github.tndavidson.geodespringbootpoc.processor.function")
public class GeodeSpringBootPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeodeSpringBootPocApplication.class, args);
    }

}
