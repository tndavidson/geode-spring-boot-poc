package com.github.tndavidson.geodespringbootpoc.feature.runner;
import com.github.tndavidson.geodespringbootpoc.SpringGeodeTestBase;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

/**
 * A test runner class configured to execute Cucumber tests with Spring Geode integration.
 *
 * This class is annotated with JUnit 5 and Cucumber-specific annotations to discover and execute
 * Cucumber feature files. It extends the {@code SpringGeodeTestBase} to leverage customized
 * Spring test configurations, specifically for GemFire/Geode testing.
 *
 * The class-level annotations include:
 *
 * - {@code @Suite}: Indicates that the class is a JUnit 5 suite.
 * - {@code @IncludeEngines("cucumber")}: Specifies the Cucumber engine for test execution.
 * - {@code @SelectClasspathResource("features")}: Points to the location of the Cucumber feature files.
 * - {@code @ConfigurationParameter(key, value)}: Configures additional Cucumber options, such as:
 *    - {@code key = GLUE_PROPERTY_NAME}: Indicates the package where step definitions reside.
 *    - {@code key = PLUGIN_PROPERTY_NAME}: Configures Cucumber plugins, such as "pretty" for formatted output
 *      and "summary" for test summaries.
 *
 * By extending {@code SpringGeodeTestBase}, this class integrates with a mocked Apache Geode environment and
 * entity-specific configurations, including the entity class {@code Customer}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // looks in: src/test/resources/features
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.github.tndavidson.geodespringbootpoc.feature.stepdef")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, summary")
public class CucumberRunner extends SpringGeodeTestBase {
}
