package com.github.tndavidson.geodespringbootpoc.processor.impl;

import com.github.tndavidson.geodespringbootpoc.model.Account;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import com.github.tndavidson.geodespringbootpoc.processor.AccountProcessor;
import com.github.tndavidson.geodespringbootpoc.repository.AccountRepository;
import com.github.tndavidson.geodespringbootpoc.repository.CustomerRepository;
import com.github.tndavidson.geodespringbootpoc.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of the {@link AccountProcessor} interface, responsible for handling operations
 * related to accounts and transactions. This class provides functionality for retrieving
 * accounts and transactions, creating new accounts and transactions, and recalculating
 * account balances based on related transactions.
 *
 * This class uses {@link CustomerRepository}, {@link AccountRepository}, and {@link TransactionRepository}
 * to interact with the data layer.
 */
@Component
public class AccountProcessorImpl implements AccountProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountProcessorImpl.class);

    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public AccountProcessorImpl(CustomerRepository customerRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves an {@link Account} by its unique identifier.
     * If no account is found with the provided identifier, an exception is thrown.
     *
     * @param accountId the unique identifier of the account to be retrieved
     * @return the {@link Account} associated with the given identifier
     * @throws ResponseStatusException if the account with the specified identifier is not found
     */
    @Override
    public Account findAccountById(String accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    /**
     * Creates a new account for a customer. The method verifies the existence of the customer
     * associated with the provided account information. If the customer is not found, an exception
     * is thrown. It initializes the account with a unique identifier and a zero balance before
     * saving it to the repository.
     *
     * @param account the {@link Account} object containing the details of the account to be created.
     *                The {@code customerId} field must be present and linked to an existing customer.
     * @return the {@link Account} object after it has been saved to the repository, with its
     *         {@code accountId} and {@code balance} fields populated.
     * @throws ResponseStatusException if the customer associated with the provided account
     *                                 information is not found.
     */
    @Override
    public Account createAccount(Account account) {
        customerRepository.findById(account.getCustomerId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        account.setAccountId(UUID.randomUUID().toString());
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        return account;
    }

    /**
     * Creates a new transaction associated with an existing account. The method validates
     * the existence of the account linked to the provided transaction. If the account is not
     * found, an exception is thrown. The transaction is initialized with a unique identifier,
     * current timestamp, and a cleared status of false before being saved to the repository.
     *
     * @param transaction the {@link Transaction} object containing the details of the transaction
     *                    to be created. The {@code accountId} field in the transaction must be
     *                    associated with an existing account.
     * @return the {@link Transaction} object after it has been saved to the repository, with its
     *         {@code transactionId}, {@code timestamp}, and {@code cleared} fields populated.
     * @throws ResponseStatusException if the account associated with the transaction is not found.
     */
    @Override
    public Transaction createTransaction(Transaction transaction) {
        accountRepository.findById(transaction.getAccountId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setTimestamp(Instant.now());
        transaction.setCleared(false);
        transactionRepository.save(transaction);

        return transaction;
    }

    /**
     * Retrieves a {@link Transaction} by its unique identifier. If no transaction is found
     * with the provided identifier, a {@link ResponseStatusException} with HTTP status 404 is thrown.
     *
     * @param transactionId the unique identifier of the transaction to be retrieved
     * @return the {@link Transaction} associated with the given identifier
     * @throws ResponseStatusException if the transaction with the specified identifier is not found
     */
    @Override
    public Transaction findTransactionById(String transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    /**
     * Recalculates the balance of an account by summing up the amounts of all its associated transactions.
     * If the account is not found by its unique identifier, an exception is thrown.
     * Once the balance is recalculated, the account's balance and balance calculation date
     * are updated and saved to the repository.
     *
     * @param accountId the unique identifier of the account for which the balance will be recalculated.
     *                  This identifier is used to retrieve the account and its transactions.
     * @throws RuntimeException if the account with the specified identifier is not found.
     */
    @Override
    public void recalculateBalance(String accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(
                () -> new RuntimeException("accountId not found when attempting to recalculate balance: " + accountId));

        LOGGER.debug("recalculating balance for accountId: {} oldBalance: {} lastCalculatedDate: {}",
                accountId, account.getBalance(), account.getBalanceCalculatedDate());

        var accountTransactions = transactionRepository.findByAccountId(accountId);

        LOGGER.debug("found {} transactions for accountId: {}", accountTransactions.size(), accountId);

        var newBalance = BigDecimal.ZERO;

        for (Transaction transaction : accountTransactions) {
            newBalance = newBalance.add(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP);
        }

        LOGGER.debug("Calculated new balance of {} for accountId: {}", newBalance, accountId);

        account.setBalance(newBalance);
        account.setBalanceCalculatedDate(Instant.now());

        accountRepository.save(account);
    }
}
