package com.github.tndavidson.geodespringbootpoc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Indexed;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.io.Serial;
import java.io.Serializable;

@Region("customer")
@Data
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = -4045982733837823723L;

    @Id
    private String id;

    private String firstName;

    @Indexed
    private String lastName;
}
