package com.github.tndavidson.geodespringbootpoc;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.tests.mock.annotation.EnableGemFireMockObjects;

/**
 * Base class for testing Spring applications with Geode integration.
 *
 * This class provides a pre-configured test environment for applications utilizing
 * Apache Geode. It integrates Spring Data for Geode configurations, enabling the use
 * of mocked Geode objects and predefined data regions during testing.
 *
 * Features enabled by this class:
 *
 * - Geode Mocking: Annotated with {@code @EnableGemFireMockObjects} to use a mocked
 *   Geode environment, simplifying tests without requiring a running Geode instance.
 * - Entity-Defined Regions: Configures regions based on annotated entity classes
 *   through {@code @EnableEntityDefinedRegions}. This uses the `Customer` entity to
 *   define a region with {@code ClientRegionShortcut.LOCAL}.
 */
@EnableGemFireMockObjects
@EnableEntityDefinedRegions(basePackageClasses = {Customer.class},
        clientRegionShortcut = ClientRegionShortcut.LOCAL)
public class SpringGeodeTestBase {
}
