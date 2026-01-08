package com.github.tndavidson.geodespringbootpoc.function;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.ResultSender;
import org.apache.geode.cache.query.SelectResults;

import java.util.List;

/**
 * The DepositHistoryServerSideFunction is a server-side implementation of the Function interface
 * that retrieves deposit transaction history for a specific customer. It fetches all accounts
 * associated with a given customer and retrieves deposit transactions for those accounts.
 *
 * This function is intended to be executed within a distributed computing environment, such as a
 * Geode cache.
 *
 * The function performs the following tasks:
 * 1. Retrieves the customer's accounts based on the provided customer ID.
 * 2. Queries for transactions associated with the retrieved accounts where the transaction
 *    amount is greater than zero (denoting deposits).
 * 3. Returns the list of transactions as the function result.
 *
 * Key Details:
 * - The function uses GemFire OQL (Object Query Language) to query the cache for both accounts
 *   and transactions.
 * - In the event of no accounts being found for the provided customer, an exception is thrown.
 * - Exceptions encountered during execution are sent back to the client using the ResultSender.
 *
 * This function is registered with an ID defined by the constant {@code ID}.
 * It is typically invoked using function execution frameworks provided by Geode.
 *
 * Thread-Safety:
 * - This class is thread-safe and designed to be used in concurrent execution contexts.
 *
 * Error Handling:
 * - Exceptions during OQL execution or other runtime issues are captured and sent as part
 *   of the ResultSender to ensure proper client-side error handling.
 */
public class DepositHistoryServerSideFunction implements Function<Customer> {

    public static final String ID = "DEPOSIT_HISTORY_SERVER_SIDE_FUNCTION";

    @Override
    public void execute(FunctionContext<Customer> context) {
        final ResultSender<List<Transaction>> resultSender = context.getResultSender();

        try {
            Object args = context.getArguments();
            Customer customer;

            if (args instanceof Customer) {
                customer = (Customer) args;
            } else if (args instanceof Object[] && ((Object[]) args).length > 0 && ((Object[]) args)[0] instanceof Customer) {
                customer = (Customer) ((Object[]) args)[0];
            } else {
                throw new IllegalArgumentException("Expected Customer object as argument, but received: "
                        + (args == null ? "null" : args.getClass().getName()));
            }

            var cache = CacheFactory.getAnyInstance();
            var queryService = cache.getQueryService();

            var accountQueryOql = "select a.accountId from /account a where a.getCustomerId() = $1";
            var accountQuery = queryService.newQuery(accountQueryOql);
            var accountSelectResults = (SelectResults<String>) accountQuery.execute(customer.getId());

            if (accountSelectResults == null || accountSelectResults.isEmpty()) {
                throw new RuntimeException("No accounts found for customer with id: " + customer.getId());
            }

            var accounts = accountSelectResults.stream()
                    .map(accountId -> "'" + accountId + "'")
                    .toList();

            var transactionQueryOql = new StringBuilder().append("select * ")
                    .append("from /transaction t ")
                    .append("where t.accountId in set(")
                    .append(String.join(", ", accounts))
                    .append(") ")
                    .append("and t.amount > 0");

            var transactionQuery = queryService.newQuery(transactionQueryOql.toString());
            var transactionSelectResults = (SelectResults<Transaction>) transactionQuery.execute();

            // Convert the non-serializable SelectResults wrapper to a standard ArrayList
            var resultsAsList = transactionSelectResults.stream().toList();

            context.getResultSender().lastResult(resultsAsList);

        } catch (Exception e) {
            resultSender.sendException(e);
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
