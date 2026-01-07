package com.github.tndavidson.geodespringbootpoc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Region("account")
@Data
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = -5190020909160796399L;

    @Id
    private String accountId;

    private String customerId;

    private BigDecimal balance;

    private Instant balanceCalculatedDate;
}
