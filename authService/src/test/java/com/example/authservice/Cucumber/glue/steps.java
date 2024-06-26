package com.example.authservice.Cucumber.glue;
import com.example.authservice.Repository.EndUserRepository;
import com.example.authservice.Repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.Assert.*;

public class steps {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EndUserRepository endUserRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Given("the login API is available at {string}")
    public void the_login_API_is_available_at(String url) {

    }
    @Given("token-api is available at {string}")
    public void token_api_is_available(String url){

    }
    @When("POST request to {string} with valid credentials")
    public void POST_request_with_valid_credentials(String url, DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);

        // Extract username and password
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Create JSON request body
        String requestBody = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HTTP entity with the request body and headers
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send POST request to the specified URL
        response = restTemplate.postForEntity(url, entity, String.class);

        // Print response for debugging
        System.out.println("Response: " + response.getBody());
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode().value());
    }
    @And("the response should contain a token field")
    public void response_should_have_token(){
        assertTrue(response.getBody().contains("token"));
    }
    @When("send a POST request to {string} with invalid credentials")
    public void POST_request_with_invalid_credentials(String url , DataTable dataTable){
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);

        String username = credentials.get("username");
        String password = credentials.get("password");

        String requestBody = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Response: " + response.getBody());
    }
    @And("the response should contain an error message {string}")
    public void passwordAndUsernameMissmatch(String error){
        Assertions.assertTrue(response.getBody().contains(error));
    }

    @When("I send a GET request to {string} with valid credentials for a locked account")
    public void getAPItokenForLockedAccount(String url , DataTable dataTable){
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String Authoriaztion = credentials.get("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authoriaztion" , Authoriaztion);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Response: " + response.getBody());
    }


    @Then("remove created user in this test")
    @Transactional
    public void removeCreatedUserInThisTest(DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String username  = credentials.get("username");
        endUserRepository.removeEndUserByUsername(username);
    }

    @Then("the response statues should not be {int}")
    public void theResponseStatuesShouldNotBe(int arg0) {
        Assertions.assertNotEquals(arg0 , response.getStatusCode().value());
    }


    @When("POST request to {string} with valid token and valid name and expire date")
    public void postRequestToWithValidTokenAndValidNameAndExpireDate(String arg0, DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String Authoriaztion = credentials.get("Authorization");
        String name = credentials.get("name");
        String date = credentials.get("date");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization" , Authoriaztion);
        String body = String.format("{\"name\":\"%s\", \"date\":\"%s\"}", name, date);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        response = restTemplate.postForEntity(arg0 , entity , String.class);
        System.out.println("Response: " + response.getBody());
    }

    @Given("endpoint is available at {string}")
    public void endpointIsAvailableAt(String arg0) {

    }

    @When("Post request to {string} with given token")
    public void postRequestToWithGivenToken(String arg0, DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String Authoriaztion = credentials.get("token");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization" , Authoriaztion);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = restTemplate.exchange(arg0 , HttpMethod.GET , entity, String.class);
        System.out.println("Response: " + response.getBody());
    }

    @Then("remove this token with give credentals")
    @Transactional
    public void removeThisTokenWithGiveCredentals(DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        tokenRepository.deleteByName(credentials.get("name"));
    }

    @When("Put request to {string} by given token")
    public void putRequestToByGivenToken(String arg0 , DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String Authoriaztion = credentials.get("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization" , Authoriaztion);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = restTemplate.exchange(arg0 , HttpMethod.PUT , entity, String.class);
        System.out.println("Response: " + response.getBody());
    }
}
