package com.github.tndavidson.geodespringbootpoc;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableStatistics;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * The entry point for the GeodeSpringBootPoc application.
 *
 * This class serves as the main configuration and bootstrap component for the Spring Boot application,
 * integrating with Apache Geode as a client cache application.
 *
 * Annotations used:
 * - {@code @SpringBootApplication}: Marks this as a Spring Boot application.
 * - {@code @EnableEntityDefinedRegions}: Configures Apache Geode regions based on the specified domain object class.
 * - {@code @ClientCacheApplication}: Enables the configuration of a client cache for Apache Geode.
 * - {@code @EnableGemfireRepositories}: Scans the specified base package for GemFire repository interfaces.
 * - {@code @EnableStatistics}: Enables metrics gathering
 * The application relies on a `Customer` domain class to interact with an Apache Geode region named "customer".
 */
@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = {Customer.class})
@ClientCacheApplication
@EnableGemfireRepositories(basePackages = "com.github.tndavidson.geodespringbootpoc.repository")
@EnableStatistics
public class GeodeSpringBootPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeodeSpringBootPocApplication.class, args);
    }

}
