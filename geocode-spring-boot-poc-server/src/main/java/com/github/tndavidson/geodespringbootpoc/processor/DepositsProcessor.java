package com.github.tndavidson.geodespringbootpoc.processor;

import com.github.tndavidson.geodespringbootpoc.model.Transaction;

import java.util.List;

/**
 * Interface for processing deposit transactions across multiple accounts for a specific customer.
 * Provides methods for retrieving the deposit history for all accounts associated with a given customer.
 */
public interface DepositsProcessor {
    List<Transaction> getDepositHistoryAllAccounts(String customerId);
}
