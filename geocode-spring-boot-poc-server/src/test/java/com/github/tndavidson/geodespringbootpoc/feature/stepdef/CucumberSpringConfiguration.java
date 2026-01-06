package com.github.tndavidson.geodespringbootpoc.feature.stepdef;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Configuration class used for integrating Cucumber with Spring in a test context.
 * This class ensures that the Spring application context is properly loaded and made available
 * during the execution of Cucumber tests.
 *
 * Annotations:
 * - {@code @CucumberContextConfiguration}: Marks this class as a configuration for Cucumber
 *   to use Spring's application context.
 * - {@code @SpringBootTest}: Configures the test to load the Spring application context.
 *   The attribute {@code webEnvironment} is set to {@code SpringBootTest.WebEnvironment.RANDOM_PORT},
 *   specifying that a random port should be used for web environment testing.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {
}
