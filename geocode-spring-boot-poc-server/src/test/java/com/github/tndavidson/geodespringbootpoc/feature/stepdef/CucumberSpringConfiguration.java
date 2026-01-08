package com.github.tndavidson.geodespringbootpoc.feature.stepdef;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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
// FIXME I added this DC because the continuous query in TransactionPostProcessor kept trying to register in each test,
// which was causing an exception. This is a workaround to ensure the CQ is cleaned up after each test. Not ideal to
// spin up a new context for every test, so I need to find a way to manually clean up the CQ after each test.
@DirtiesContext
public class CucumberSpringConfiguration {
}
