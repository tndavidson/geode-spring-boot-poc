package com.github.tndavidson.geodespringbootpoc.feature.stepdef.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;

import java.lang.reflect.Type;

/**
 * The TypeTransformer class is responsible for transforming objects between different types
 * using the Jackson ObjectMapper. It provides a default transformation mechanism
 * for converting source objects to target types.
 */
public class TypeTransformer {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JSR310Module());

    /**
     * Type transformer for Cucumber tables. This allows us to use convert table data to POJOs, rather than implementing
     * step defs that parse tables using the default cucumber DataTable class.
     * This method acts as a default transformer for parameterized types and data table entries.
     *
     * @param fromValue    the source object to be transformed
     * @param toValueType  the target type to which the source object should be transformed
     * @return the transformed object of the specified target type
     */
    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        JavaType javaType = objectMapper.constructType(toValueType);
        return objectMapper.convertValue(fromValue, javaType);
    }
}
