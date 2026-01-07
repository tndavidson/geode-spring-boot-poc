package com.github.tndavidson.geodespringbootpoc.processor;

import com.github.tndavidson.geodespringbootpoc.model.Account;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;

/**
 * Interface for processing and managing accounts and their associated transactions.
 * Provides methods for account creation, retrieval, and balance recalculation,
 * as well as transaction creation and retrieval.
 */
public interface AccountProcessor {
    /**
     * Retrieves an account by its unique identifier.
     *
     * @param accountId the unique identifier of the account to be retrieved
     * @return the account associated with the given identifier, or null if no account is found
     */
    Account findAccountById(String accountId);

    /**
     * Creates a new account with the specified details.
     *
     * @param account the account object containing the details of the account to be created
     * @return the newly created account object
     */
    Account createAccount(Account account);

    /**
     * Creates a new transaction with the provided transaction details.
     *
     * @param transaction the transaction object containing the details of the transaction to be created
     * @return the newly created transaction object
     */
    Transaction createTransaction(Transaction transaction);

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param transactionId the unique identifier of the transaction to be retrieved
     * @return the transaction associated with the given identifier, or null if no transaction is found
     */
    Transaction findTransactionById(String transactionId);

    /**
     * Recalculates the balance of the account specified by the given account identifier.
     * This method should be used when there are changes affecting the account balance,
     * such as new transactions, updates, or corrections.
     *
     * @param accountId the unique identifier of the account whose balance needs to be recalculated
     */
    void recalculateBalance(String accountId);
}
