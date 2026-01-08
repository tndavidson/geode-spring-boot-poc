package com.github.tndavidson.geodespringbootpoc.function;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.ResultSender;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class DepositHistoryServerSideFunctionTest {

    private DepositHistoryServerSideFunction function;

    @Mock
    private FunctionContext<Customer> context;

    @Mock
    private ResultSender<List<Transaction>> resultSender;

    @Mock
    private Cache cache;

    @Mock
    private QueryService queryService;

    @Mock
    private Query accountQuery;

    @Mock
    private Query transactionQuery;

    @Mock
    private SelectResults<String> accountSelectResults;

    @Mock
    private SelectResults<Transaction> transactionSelectResults;

    private MockedStatic<CacheFactory> mockedCacheFactory;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        function = new DepositHistoryServerSideFunction();

        mockedCacheFactory = mockStatic(CacheFactory.class);
        mockedCacheFactory.when(CacheFactory::getAnyInstance).thenReturn(cache);

        when(cache.getQueryService()).thenReturn(queryService);
        doReturn(resultSender).when(context).getResultSender();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockedCacheFactory.close();
        closeable.close();
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        doReturn(customer).when(context).getArguments();

        when(queryService.newQuery(contains("/account"))).thenReturn(accountQuery);
        when(accountQuery.execute(customer.getId())).thenReturn(accountSelectResults);
        when(accountSelectResults.isEmpty()).thenReturn(false);

        when(accountSelectResults.stream()).thenReturn(Stream.of("acc1"));

        when(queryService.newQuery(contains("/transaction"))).thenReturn(transactionQuery);
        when(transactionQuery.execute()).thenReturn(transactionSelectResults);
        when(transactionSelectResults.stream()).thenReturn(Stream.empty());

        function.execute(context);

        verify(resultSender).lastResult(anyList());
    }

    @Test
    public void testExecuteSuccessWithObjectArrayArgs() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        Object args = new Object[]{customer};
        doReturn(args).when(context).getArguments();

        when(queryService.newQuery(contains("/account"))).thenReturn(accountQuery);
        when(accountQuery.execute(customer.getId())).thenReturn(accountSelectResults);
        when(accountSelectResults.isEmpty()).thenReturn(false);

        when(accountSelectResults.stream()).thenReturn(Stream.of("acc1"));

        when(queryService.newQuery(contains("/transaction"))).thenReturn(transactionQuery);
        when(transactionQuery.execute()).thenReturn(transactionSelectResults);
        when(transactionSelectResults.stream()).thenReturn(Stream.empty());

        function.execute(context);

        verify(resultSender).lastResult(anyList());
    }

    @Test
    public void testExecuteNoAccounts() throws Exception {
        Customer customer = new Customer();
        customer.setId("cust1");
        doReturn(customer).when(context).getArguments();

        when(queryService.newQuery(contains("/account"))).thenReturn(accountQuery);
        when(accountQuery.execute(customer.getId())).thenReturn(accountSelectResults);
        when(accountSelectResults.isEmpty()).thenReturn(true);

        function.execute(context);

        verify(resultSender).sendException(any(RuntimeException.class));
    }

    @Test
    public void testExecuteException() throws Exception {
        when(context.getArguments()).thenThrow(new RuntimeException("Test exception"));

        function.execute(context);

        verify(resultSender).sendException(any(RuntimeException.class));
    }

    @Test
    public void testGetId() {
        assertEquals(DepositHistoryServerSideFunction.ID, function.getId());
    }
}