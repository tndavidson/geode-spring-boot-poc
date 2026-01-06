package com.github.tndavidson.geodespringbootpoc.repository;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This interface provides the mechanism for CRUD operations and custom query execution
 * for the {@link Customer} entity. It extends the {@code CrudRepository} interface,
 * enabling methods for persistence, retrieval, update, and deletion of {@link Customer} objects.
 *
 * The {@code findAll} method is specifically defined to retrieve all customer records
 * from the data store as a list.
 */
public interface CustomerRepository extends CrudRepository<Customer, String> {

    List<Customer> findAllByOrderByLastName();

    List<Customer>  findByLastNameLikeOrderByLastName(String lastName);
}
