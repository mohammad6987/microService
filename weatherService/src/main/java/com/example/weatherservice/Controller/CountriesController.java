package com.example.weatherservice.Controller;

import com.example.weatherservice.Exception.CountryNotFoundException;
import com.example.weatherservice.Exception.UnAuthorizedAccess;
import com.example.weatherservice.Model.*;
import com.example.weatherservice.Service.WeatherService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequestMapping("/weatherService")
public class CountriesController {

    private final RestTemplate restTemplate;
    private final WeatherService weatherService;
    private final String API_KEY = "LIPQv0O9lPlFFgxNslVs5g==sQ6DOXmXmTdOZPCm";

    public CountriesController(RestTemplate restTemplate, WeatherService weatherService) {
        this.restTemplate = restTemplate;
        this.weatherService = weatherService;

    }


    @GetMapping("/countries")
    //@Cacheable (value = "countries", key = "allCountries")
    public CountryContainer getCountry(@RequestHeader("Authorization") String token) throws UnAuthorizedAccess {
        if(!weatherService.isUserAuthorized(token)){
           throw new UnAuthorizedAccess("there is an error with your token!");
        }else{
        datatype datatype = restTemplate.getForEntity("https://countriesnow.space/api/v0.1/countries", datatype.class).getBody();
        Country[] listofCountry = datatype.getData();
        ArrayList<CountryDto> countries = new ArrayList<CountryDto>();
        for (Country country : listofCountry) {
            countries.add(new CountryDto(country.getCountry()));
        }
        return new CountryContainer(countries,countries.size());}
    }

    @GetMapping("/countries/{name}")
    @Cacheable(value = "countries", key = "#name")
    public Optional<CountryDtoForSearch> getCountryByName(@RequestHeader("Authorization") String token,@PathVariable String name) throws UnAuthorizedAccess, CountryNotFoundException {
        if(!weatherService.isUserAuthorized(token)){
            throw new UnAuthorizedAccess("there is an error with your token!");
        }else{
            CountryDtoForSearch countryDtoForSearch = weatherService.findByName(name);
            if (countryDtoForSearch.getName() != null) {
                return Optional.ofNullable(countryDtoForSearch);
            } else {
                throw new CountryNotFoundException("Country not found");
            }
        }
    }

    @GetMapping("/countries/{name}/weather")
    @Cacheable(value = "weathers" , key = "#name")
    public WeatherDto getCountryWeather(@RequestHeader("Authorization") String token, @PathVariable String name) throws UnAuthorizedAccess, CountryNotFoundException {
        if(!weatherService.isUserAuthorized(token)){
            throw new UnAuthorizedAccess("there is an error with your token!");
        }else{
        return weatherService.CountryWether(name);}
    }
}
