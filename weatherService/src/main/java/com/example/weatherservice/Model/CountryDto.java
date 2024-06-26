package com.example.weatherservice.Model;

import lombok.Data;

@Data
public class CountryDto {
    private String name;

    public CountryDto(String name) {
        this.name = name;
    }
}
