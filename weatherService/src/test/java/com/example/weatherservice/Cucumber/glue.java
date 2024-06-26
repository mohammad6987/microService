package com.example.weatherservice.Cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class glue {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;
    @Given("Endpoint is available {string}")
    public void endpointIsAvailable(String arg0) {
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int arg0) {
        assertEquals(arg0, response.getStatusCode().value());

    }

    @Then("the response status code should not be {int}")
    public void theResponseStatusCodeShouldNotBe(int arg0) {
        assertNotEquals(arg0, response.getStatusCode().value());

    }
    @When("Get request to {string} with token")
    public void getRequestToWithoutToken(String arg0 , DataTable dataTable) {
        Map<String, String> credentials = dataTable.asMap(String.class, String.class);
        String Authoriaztion = credentials.get("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization" , Authoriaztion);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = restTemplate.exchange(arg0 , HttpMethod.GET , entity , String.class);
        System.out.println("Response: " + response.getBody());
    }

    @When("Get request to {string} without token for real")
    public void getRequestToWithoutTokenForReal(String arg0) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        response = restTemplate.exchange(arg0 , HttpMethod.GET , entity , String.class);
        System.out.println("Response: " + response.getBody());
    }

    @And("the output must be {string}")
    public void theOutputMustBe(String arg0) {
        assertEquals(arg0 , response.getBody());
    }
}
