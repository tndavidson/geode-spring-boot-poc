package com.github.tndavidson.geodespringbootpoc.repository;

import com.github.tndavidson.geodespringbootpoc.model.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * This interface provides the mechanism for CRUD operations and custom query execution
 * for the {@link Account} entity. It extends the {@code CrudRepository} interface,
 * enabling methods for persistence, retrieval, update, and deletion of {@link Account} objects.
 *
 * By default, this repository enables interaction with the "account" region in a Geode
 * data grid, allowing for the storage and manipulation of {@link Account} instances,
 * indexed by their unique {@code accountId}.
 */
public interface AccountRepository extends CrudRepository<Account, String> {

}
