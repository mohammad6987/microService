package com.example.authservice.Cucumber;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;

import java.lang.reflect.Type;

public class CucumberConfiguration {
    private final ObjectMapper mapper;

    public CucumberConfiguration() {
        mapper = new ObjectMapper();
    }
    @DefaultDataTableCellTransformer
    @DefaultDataTableEntryTransformer
    @DefaultParameterTransformer
    public Object transform(final Object from, final Type to){
        return mapper.convertValue(from , mapper.constructType(to));
    }
}
