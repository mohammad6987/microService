package com.example.weatherservice.Service;

import com.example.weatherservice.Exception.CountryNotFoundException;
import com.example.weatherservice.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Cacheable(value = "countries", key = "#name")
    public CountryDtoForSearch findByName(String name) {
        String url = "https://api.api-ninjas.com/v1/country?name=" + name;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "LIPQv0O9lPlFFgxNslVs5g==sQ6DOXmXmTdOZPCm");
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Country[]> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, request, Country[].class
        );
        CountryDtoForSearch result = new CountryDtoForSearch();
        for (Country country : responseEntity.getBody()) {
            result = new CountryDtoForSearch(country.getName(), country.getCapital(), country.getIso2(), country.getPopulation(), country.getPop_growth(), country.getCurrency());
        }
        return (result);
    }


    @Cacheable(value = "weathers" , key = "#name")
    public WeatherDto CountryWether(String name) throws CountryNotFoundException {
        String capitalName = null;
        String countryName = null;
        String url1 = "https://api.api-ninjas.com/v1/country?name=" + name;
        HttpHeaders headers1 = new HttpHeaders();
        headers1.set("x-api-key", "LIPQv0O9lPlFFgxNslVs5g==sQ6DOXmXmTdOZPCm");
        HttpEntity<Void> request1 = new HttpEntity<>(headers1);
        ResponseEntity<Country[]> responseEntity1 = restTemplate.exchange(
                url1, HttpMethod.GET, request1, Country[].class
        );
        if (responseEntity1.getBody().length == 0) {
            throw new CountryNotFoundException("Country not found");
        } else {
            for (Country country1 : responseEntity1.getBody()) {
                capitalName = country1.getCapital();
                countryName = country1.getName();
            }
            WeatherDto finalWeather = new WeatherDto();
            String url = "https://api.api-ninjas.com/v1/weather?city=" + capitalName;
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", "LIPQv0O9lPlFFgxNslVs5g==sQ6DOXmXmTdOZPCm");
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Weather> responseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, request, Weather.class
            );
            finalWeather.setCountry_name(countryName);
            finalWeather.setCapital(capitalName);
            finalWeather.setHumidity(responseEntity.getBody().getHumidity());
            finalWeather.setTemp(responseEntity.getBody().getTemp());
            finalWeather.setWind_speed(responseEntity.getBody().getWind_speed());
            finalWeather.setWind_degrees(responseEntity.getBody().getWind_degrees());
            return finalWeather;
        }
    }
    public boolean isUserAuthorized(String token) {

        String url = "http://auth-container:8080/authService/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        System.out.println(token+" in here");
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(url , HttpMethod.GET , entity , Boolean.class);
            System.out.println(response.getBody());
            if(response.getStatusCode() == HttpStatusCode.valueOf(403))
                return false;
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }
}
