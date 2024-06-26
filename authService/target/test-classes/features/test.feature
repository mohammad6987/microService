Feature: all possible scenarios
  Scenario: Successful login
    Given the login API is available at "/authService/users/login"
    When POST request to "/authService/users/login" with valid credentials
      | username | abcdefg |
      | password | 123     |
    Then the response status code should be 200
    And the response should contain a token field

  Scenario: Unsuccessful login due to incorrect password
    Given the login API is available at "/authService/users/login"
    When send a POST request to "/authService/users/login" with invalid credentials
      | username | abcdefg |
      | password | 1234    |
    Then the response status code should be 404
    And the response should contain an error message "username and password doesn't match!"

  Scenario: Unsuccessful login due to locked account
    Given the login API is available at "/authService/users/login"
    When I send a GET request to "/authService/users/api-tokens" with valid credentials for a locked account
      | Authorization |API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNkZWYiLCJleHAiOjE3MTk0Mjg5MDV9.0YEIy_bTVQjeJcMEjsd3Qj2xKwLWivYmAW_un_4sPGzIJ4Wmxzrnc2wZcIRDJ7y1VUOjQiTtD7ZOZlEolDqdGA|
    Then the response status code should be 403

  Scenario: Successful register
    Given token-api is available at "/authService/user/api-tokens"
    When POST request to "/authService/users/register" with valid credentials
      | username | qwertyuiop |
      | password | 123     |
    Then the response status code should be 200
    And the response should contain an error message "please wait until the admin authenticate your account"
    Then remove created user in this test
      | username | qwertyuiop |

  Scenario: Unsuccessful register
    Given token-api is available at "/authService/user/api-tokens"
    When POST request to "/authService/users/register" with valid credentials
      | username | qwertyuiop |
      | password | 123     |
    Then the response status code should be 200
    When POST request to "/authService/users/register" with valid credentials
      | username | qwertyuiop |
      | password | 123     |
    Then the response status code should be 409
    Then remove created user in this test
      | username | qwertyuiop |

  Scenario: missing parameters in register
    Given token-api is available at "/authService/user/api-tokens"
    When POST request to "/authService/users/register" with valid credentials
      | username | qwertyuiop |
      | password |            |
    Then the response status code should be 200
    Then remove created user in this test
      | username | qwertyuiop |
  Scenario: validate token
    Given endpoint is available at "/authService/validate"
    When Post request to "/authService/validate" with given token
      |token | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 200


  Scenario: Succcessful token creation and remove
    Given token-api is available at "/authService/user/api-tokens"
    When POST request to "/authService/user/api-tokens" with valid token and valid name and expire date
      |name | xxxxxXXXX            |
      |date | 2025-05-25T19:55:59Z |
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 201
    Then remove this token with give credentals
      |name | xxxxxXXXX            |

  Scenario: already choosen token name
    Given token-api is available at "/authService/user/api-tokens"
    When POST request to "/authService/user/api-tokens" with valid token and valid name and expire date
      |name | prime_token   |
      |date | 2025-05-25T19:55:59Z |
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxIiwiZXhwIjoxODkzNTI5Nzk5fQ.xV4GszMvav82b2eDJYo3yLBuig8okOePinLWmmLBOlQyNWdKMyABbZyTBvfW23zDQ1UM07jyi-BHp3eEWZcNhw|
    Then the response status code should be 406

  Scenario: Activating a user by admin token
    Given token-api is available at "/authService/admin/username=abc&active=true"
    When Put request to "/authService/admin/username=q&active=true" by given token
    |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbiIsImV4cCI6MTg2MTk5Mzc5OX0.ZGAcPIlEKv86uWqVLd6VDttxV7jMOWNBfGBvx27i0lUyiDYtn4SD8ub7rCrayvwa-HjVyW9PyjGV1kZwUOUqAw|
    Then the response status code should be 200

  Scenario: Activating a not-exsiting user
    Given token-api is available at "/authService/admin/username=abcdefg&active=true"
    When Put request to "/authService/admin/username=xxxxxxxxx&active=true" by given token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbiIsImV4cCI6MTg2MTk5Mzc5OX0.ZGAcPIlEKv86uWqVLd6VDttxV7jMOWNBfGBvx27i0lUyiDYtn4SD8ub7rCrayvwa-HjVyW9PyjGV1kZwUOUqAw|
    Then the response statues should not be 200

  Scenario: Deactivating a user by admin token
    Given token-api is available at "/authService/admin/username=abc&active=true"
    When Put request to "/authService/admin/username=q&active=false" by given token
      |Authorization | API eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbiIsImV4cCI6MTg2MTk5Mzc5OX0.ZGAcPIlEKv86uWqVLd6VDttxV7jMOWNBfGBvx27i0lUyiDYtn4SD8ub7rCrayvwa-HjVyW9PyjGV1kZwUOUqAw|
    Then the response status code should be 200

  Scenario: Trying to activate a user without admin token
    Given token-api is available at "/authService/admin/username=abcdefg&active=true"
    When Put request to "/authService/admin/username=q&active=true" by given token
      |Authorization | API eyJhb |
    Then the response status code should be 403