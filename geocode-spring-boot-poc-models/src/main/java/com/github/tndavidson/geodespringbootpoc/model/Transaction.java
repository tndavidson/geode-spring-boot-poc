package com.github.tndavidson.geodespringbootpoc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Indexed;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Region("transaction")
@Data
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 6819052593058996481L;

    @Id
    private String transactionId;

    @Indexed
    private String accountId;

    private Instant timestamp;

    private String description;

    private BigDecimal amount;

    @Indexed
    private boolean cleared;
}
