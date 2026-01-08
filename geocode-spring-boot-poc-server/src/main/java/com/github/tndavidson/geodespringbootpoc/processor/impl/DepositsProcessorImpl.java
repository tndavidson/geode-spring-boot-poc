package com.github.tndavidson.geodespringbootpoc.processor.impl;

import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import com.github.tndavidson.geodespringbootpoc.processor.DepositsProcessor;
import com.github.tndavidson.geodespringbootpoc.processor.function.TransactionFunctions;
import com.github.tndavidson.geodespringbootpoc.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * The DepositsProcessorImpl class provides an implementation of the {@link DepositsProcessor} interface.
 * It is responsible for retrieving the deposit transaction history across all accounts for a specified customer.
 * This implementation uses the {@link CustomerRepository} to retrieve customer information
 * and the {@link TransactionFunctions} to fetch deposit transaction data.
 *
 * The class is marked as a Spring {@code @Component}, making it eligible for component scanning and
 * dependency injection.
 */
@Component
public class DepositsProcessorImpl implements DepositsProcessor {

    private final CustomerRepository customerRepository;
    private final TransactionFunctions transactionFunctions;

    public DepositsProcessorImpl(CustomerRepository customerRepository, TransactionFunctions transactionFunctions) {
        this.customerRepository = customerRepository;
        this.transactionFunctions = transactionFunctions;
    }

    @Override
    public List<Transaction> getDepositHistoryAllAccounts(String customerId) {
        var customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        return transactionFunctions.getDepositHistoryAllAccounts(customer);
    }
}
