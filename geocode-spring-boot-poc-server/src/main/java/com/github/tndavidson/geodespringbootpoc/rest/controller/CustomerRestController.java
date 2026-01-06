package com.github.tndavidson.geodespringbootpoc.rest.controller;

import com.github.tndavidson.geodespringbootpoc.model.Customer;
import com.github.tndavidson.geodespringbootpoc.repository.CustomerRepository;
import net.datafaker.Faker;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing customer-related operations.
 * This controller provides several endpoints that interact with the customer data storage
 * and offer CRUD functionalities as well as a utility for generating mock customer data.
 * <p>
 * Endpoints:
 * - Retrieve all customers.
 * - Retrieve a specific customer by their ID.
 * - Create a new customer.
 * - Generate fake customer data for testing purposes.
 * <p>
 * The controller interacts with the {@link CustomerRepository} for persistence operations.
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    private final CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam(required = false) String lastName) {
        List<Customer> customers = null;

        if(!StringUtils.hasLength(lastName)) {
            customers = customerRepository.findAllByOrderByLastName();
        } else {
            customers = customerRepository.findByLastNameLikeOrderByLastName(lastName);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        var customer = customerRepository.findById(id);

        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        customer.setId(UUID.randomUUID().toString());
        customerRepository.save(customer);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping(value = "/generate-fake-data/{numberOfRecords}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> generateFakeData(@PathVariable int numberOfRecords) {
        var faker = new Faker();

        var customers = new ArrayList<Customer>();

        for (int i = 0; i < numberOfRecords; i++) {
            var customer = new Customer();
            customer.setFirstName(faker.name().firstName());
            customer.setLastName(faker.name().lastName());
            customers.add(this.createCustomer(customer).getBody());
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
