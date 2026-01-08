Feature: This set of tests demonstrates basic object cacheing using spring-boot and Geode.
  It takes advantage of spring's local embedded Geode environment mocking capability, allowing for integration testing
  with the JPA repositories.

  Background:
    Given the following entries exists in the "customer" region
      | id                                   | firstName | lastName |
      | 018df6ca-a285-4842-bcf8-1827f6a9c2c0 | Frank     | Rizzo    |
      | 8597395b-0130-466d-88b1-3e42125f46e2 | Alice     | Johnson  |
      | f5697d81-817c-482a-89a3-5c8e37d57077 | Bob       | Smith    |
      | 674512c0-3491-4d7a-9a99-923f063a89e4 | Charlie   | Brown    |
      | 960908c6-2c5e-4903-89b1-7a7000d68f23 | David     | Williams |
      | d304f58f-2834-406c-829d-434856f70a90 | Eve       | Jones    |
      | 210e7681-4204-4c8a-9824-c896564e9a8d | Frank     | Miller   |
      | 47530843-7f2a-4f51-9686-21808603683a | Grace     | Davis    |
      | b1e8d975-5231-4043-9838-892437036329 | Hank      | Wilson   |
      | 2e7a1768-382a-4395-8884-605672803816 | Ivy       | Taylor   |
      | a9687413-5684-4869-8263-637951058473 | Jack      | Davidson |

  Scenario: Retrieve customer by id
    When a GET request is made to "/customer/018df6ca-a285-4842-bcf8-1827f6a9c2c0"
    Then the service will return a 200 response
    And the response contains the payload
      """json
        {
          "id": "018df6ca-a285-4842-bcf8-1827f6a9c2c0",
          "firstName": "Frank",
          "lastName": "Rizzo"
        }
      """

  Scenario: Retrieve customer by last name
    When a GET request is made to "/customer" with lastName: "Davi%"
    Then the service will return a 200 response
    And the response contains the payload
      """json
        [
          {
            "id": "a9687413-5684-4869-8263-637951058473",
            "firstName": "Jack",
            "lastName": "Davidson"
          },
          {
            "id": "47530843-7f2a-4f51-9686-21808603683a",
            "firstName": "Grace",
            "lastName": "Davis"
          }
        ]
      """

  Scenario: Create customer
    When a POST request is made to "/customer"
      """json
        {
          "firstName": "John",
          "lastName": "Smith"
        }
      """
    Then the service will return a 200 response
    And the response payload will contain the customer entry with id populated
    And the customer entry will be stored in the cache
