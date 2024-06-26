package com.example.authservice.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class TokenDto {
    private String name;
    private String tokenValue;
    private String date;
}
