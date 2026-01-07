package com.github.tndavidson.geodespringbootpoc.repository;

import com.github.tndavidson.geodespringbootpoc.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations and custom queries on the {@link Transaction} entity.
 * This interface extends the {@code CrudRepository} interface, which provides built-in methods for basic
 * persistence, retrieval, update, and deletion of {@link Transaction} objects.
 *
 * The repository interacts with the "transaction" region in the Geode data grid, allowing efficient
 * storage and retrieval of transaction records based on various criteria.
 *
 * Custom query methods defined in this repository include:
 *
 * - {@code findByAccountId(String accountId)}: Retrieves a list of transactions associated with
 *   a specific {@code accountId}.
 */
public interface TransactionRepository extends CrudRepository<Transaction, String> {

    List<Transaction> findByAccountId(String accountId);

}
