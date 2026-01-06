package com.github.tndavidson.geodespringbootpoc.feature.stepdef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tndavidson.geodespringbootpoc.model.Customer;
import com.github.tndavidson.geodespringbootpoc.repository.CustomerRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.List;

public class CucumberStepDefs {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private Integer localServerPort;

    private ValidatableResponse response;
    private String customerId;

    @Given("the following entries exists in the {string} region")
    public void anEntryExistsInTheRegion(String region, List<Customer> customer) {
        customerRepository.saveAll(customer);
    }

    @When("a GET request is made to {string}")
    public void aGETRequestIsMadeTo(String url) {
        response = RestAssured.given(requestSpecification()).when().get(url).then();
    }

    @Then("the response contains the payload")
    public void theResponseContainsThePayload(String expectedResponse) throws JSONException {
        var actualResponse = response.extract().asString();
        JSONAssert.assertEquals(expectedResponse, actualResponse, true);
    }

    @Then("the service will return a {int} response")
    public void theServiceWillReturnAResponse(int statusCode) {
        response.statusCode(statusCode);
    }

    @When("a GET request is made to {string} with lastName: {string}")
    public void aGETRequestIsMadeToWithLastName(String url, String lastName) {
        response = RestAssured.given(requestSpecification()).when()
                .queryParam("lastName", lastName)
                .get(url).then();
    }

    @When("a POST request is made to {string}")
    public void aPOSTRequestIsMadeTo(String url, String body) {
        response = RestAssured.given(requestSpecification()).when()
                .body(body)
                .post(url).then();
    }

    @And("the response payload will contain the customer entry with id populated")
    public void theResponsePayloadWillContainTheCustomerEntryWithIdPopulated() throws JsonProcessingException {
        var actualResponse = response.extract().asString();
        var newCustomer = objectMapper.readValue(actualResponse, Customer.class);

        Assertions.assertThat(newCustomer.getId()).isNotNull();
        customerId = newCustomer.getId();
    }

    @And("the customer entry will be stored in the cache")
    public void theCustomerEntryWillBeStoredInTheCache() {
        var expectedCustomer = new Customer();
        expectedCustomer.setId(customerId);
        expectedCustomer.setFirstName("John");
        expectedCustomer.setLastName("Smith");

        var actualCustomer = customerRepository.findById(customerId).get();

        Assertions.assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }

    private RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:" + localServerPort)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .setAccept(MediaType.APPLICATION_JSON_VALUE).build();
    }

}
