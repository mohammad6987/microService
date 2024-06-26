package com.example.weatherservice.Model;


import lombok.Data;

@Data
public class WeatherDto {
    private String country_name;
    private String capital;
    private double wind_speed;
    private int wind_degrees;
    private int temp;
    private int humidity;
}
