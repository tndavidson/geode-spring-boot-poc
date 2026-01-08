package com.github.tndavidson.geodespringbootpoc.rest.controller;

import com.github.tndavidson.geodespringbootpoc.model.Account;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import com.github.tndavidson.geodespringbootpoc.processor.AccountProcessor;
import com.github.tndavidson.geodespringbootpoc.processor.DepositsProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling account and transaction-related operations.
 * Provides endpoints for retrieving and creating accounts and transactions.
 */
@RestController
public class AccountRestController {
    private final AccountProcessor accountProcessor;
    private final DepositsProcessor depositsProcessor;

    public AccountRestController(AccountProcessor accountProcessor, DepositsProcessor depositsProcessor) {
        this.accountProcessor = accountProcessor;
        this.depositsProcessor = depositsProcessor;
    }

    @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
        return new ResponseEntity<>(accountProcessor.findAccountById(accountId), HttpStatus.OK);
    }

    @PostMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountProcessor.createAccount(account), HttpStatus.OK);
    }

    @GetMapping(value = "/transaction/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> getTransaction(@PathVariable String transactionId) {
        return new ResponseEntity<>(accountProcessor.findTransactionById(transactionId), HttpStatus.OK);
    }

    @PostMapping(value = "/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(accountProcessor.createTransaction(transaction), HttpStatus.OK);
    }

    @GetMapping(value = "/customer/{customerId}/deposit-history-all-accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getDepositHistoryAllAccounts(@PathVariable String customerId) {
        return new ResponseEntity<>(depositsProcessor.getDepositHistoryAllAccounts(customerId), HttpStatus.OK);
    }
}
