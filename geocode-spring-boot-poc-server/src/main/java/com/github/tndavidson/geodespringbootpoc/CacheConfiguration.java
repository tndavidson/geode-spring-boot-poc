package com.github.tndavidson.geodespringbootpoc;

import org.apache.geode.pdx.PdxSerializer;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining cache-related beans and settings.
 *
 * This class is annotated with {@code @Configuration} to indicate that it contains
 * bean definitions for the application's configuration related to caching, particularly
 * with Apache Geode.
 *
 * Bean declarations:
 * - {@code @Bean PdxSerializer}: Provides a {@link ReflectionBasedAutoSerializer}
 *   configured to serialize objects in the specified package. This serializer is
 *   used to handle serialization and deserialization of objects stored in the
 *   Apache Geode cache.
 */
@Configuration
public class CacheConfiguration {

    @Bean
    public PdxSerializer pdxSerializer() {
        return new ReflectionBasedAutoSerializer("com.github.tndavidson.geodespringbootpoc.model");
    }
}
