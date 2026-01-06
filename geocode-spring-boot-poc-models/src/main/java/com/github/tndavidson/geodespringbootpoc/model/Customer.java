package com.github.tndavidson.geodespringbootpoc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Indexed;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.io.Serializable;

@Region("customer")
@Data
public class Customer implements Serializable {

    @Id
    private String id;

    private String firstName;

    @Indexed
    private String lastName;
}
