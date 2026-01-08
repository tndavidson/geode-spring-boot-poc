package com.github.tndavidson.geodespringbootpoc.processor.function;

import com.github.tndavidson.geodespringbootpoc.function.DepositHistoryServerSideFunction;
import com.github.tndavidson.geodespringbootpoc.model.Customer;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnServer;

import java.util.List;

/**
 * Provides server-side transaction-related operations for customer accounts
 * within a distributed cache environment.
 *
 * This interface defines functions that facilitate the retrieval of transaction data
 * based on specific criteria. It is expected to be used in conjunction with implementations
 * of server-side functions, such as Geode functions.
 */
@OnServer
public interface TransactionFunctions {

    /**
     * Retrieves the deposit transaction history for all accounts associated with the given customer.
     * This method executes a server-side function to fetch transactions where the amount is greater than zero
     * (denoting deposits) for all accounts linked to the specified customer.
     *
     * @param customer The customer whose accounts' deposit transaction history is to be retrieved.
     *                 The customer's ID is used to identify the accounts.
     * @return A list of transactions representing the deposit history across all accounts for the given customer.
     */
    @FunctionId(DepositHistoryServerSideFunction.ID)
    List<Transaction> getDepositHistoryAllAccounts(Customer customer);
}
