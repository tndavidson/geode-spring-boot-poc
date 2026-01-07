package com.github.tndavidson.geodespringbootpoc.processor.impl;

import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import com.github.tndavidson.geodespringbootpoc.processor.AccountProcessor;
import com.github.tndavidson.geodespringbootpoc.processor.TransactionPostProcessor;
import org.apache.geode.cache.query.CqEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link TransactionPostProcessor} interface that processes
 * new transaction events received from a continuous query.
 *
 * This class listens to events associated with transactions that have not
 * been cleared and triggers the recalculation of the account balance to ensure
 * consistency after processing the transaction.
 *
 * The continuous query is configured to monitor the transaction region and select
 * transactions where the `isCleared` property is false.
 *
 * Component annotation allows it to be detected and managed as a Spring component.
 */
@Component
public class TransactionPostProcessorImpl implements TransactionPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionPostProcessorImpl.class);

    private final AccountProcessor accountProcessor;

    public TransactionPostProcessorImpl(AccountProcessor accountProcessor) {
        this.accountProcessor = accountProcessor;
    }

    /**
     * Processes a new transaction event received from a continuous query. This method
     * handles events for transactions that have not been cleared and triggers a
     * recalculation of the account balance for the associated account.
     *
     * @param event the continuous query event containing information about the new transaction.
     *              The event provides access to the transaction object and its relevant details.
     */
    @ContinuousQuery(name = "TransactionHandler", query = "select * from /transaction t where t.isCleared() = false")
    public void processNewTransaction(CqEvent event) {
        LOGGER.debug("TransactionHandler processed event: {}", event);

        var transaction = (Transaction)event.getNewValue();
        accountProcessor.recalculateBalance(transaction.getAccountId());
    }
}
