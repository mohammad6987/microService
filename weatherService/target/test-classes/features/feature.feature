# Created by Lenovo at 6/27/2024
Feature: all possible scenarios

  Scenario: Trying to access the endpoints with out token
    Given Endpoint is available "/weatherService/countries"
    When Get request to "/weatherService/countries" without token for real
    Then the response status code should be 400

  Scenario: Trying to access the endpoints with incomplete token
    Given Endpoint is available "/weatherService/countries"
    When Get request to "/weatherService/countries" with token
      |Authorization | |
    Then the response status code should be 403

  Scenario: Accessing all countries endpoint without problem
    Given Endpoint is available "/weatherService/countries"
    When Get request to "/weatherService/countries" with token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 200

  Scenario: Get request to get specific country with the right token
    Given Endpoint is available "/weatherService/countries/Germany"
    When Get request to "/weatherService/countries/Germany" with token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 200

  Scenario: Get request to get specific country without token
    Given Endpoint is available "/weatherService/countries/Germany"
    When Get request to "/weatherService/countries/Germany" without token for real
    Then the response status code should be 400

  Scenario: Get request to get specific country with the incomplete token
    Given Endpoint is available "/weatherService/countries/USA"
    When Get request to "/weatherService/countries/USA" with token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 403

  Scenario: Accessing non-existent country
    When Get request to "/weatherService/countries/USSR" with token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 400

  Scenario: Accessing non-existent country without token
    When Get request to "/weatherService/countries/USSR" without token for real
     Then the response status code should be 400

  Scenario: Accessing non-existent country with incomplete token
    When Get request to "/weatherService/countries/USSSSR" with token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIODkzNTI5NePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 403
    And the output must be "Your token doesn't work , please check the syntax or use another token"

  Scenario: Get Peru weather
    When Get request to "/weatherService/countries/Peru/" with token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 404
