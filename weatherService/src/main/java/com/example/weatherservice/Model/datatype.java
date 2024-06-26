package com.example.weatherservice.Model;

import lombok.Data;

@Data
public class datatype {
    private String error;
    private String msg;
    private Country[] data;
}
