package com.github.tndavidson.geodespringbootpoc.rest.controller;

import com.github.tndavidson.geodespringbootpoc.model.Account;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import com.github.tndavidson.geodespringbootpoc.processor.AccountProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling account and transaction-related operations.
 * Provides endpoints for retrieving and creating accounts and transactions.
 */
@RestController
public class AccountRestController {
    private final AccountProcessor accountProcessor;

    public AccountRestController(AccountProcessor accountProcessor) {
        this.accountProcessor = accountProcessor;
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
}
