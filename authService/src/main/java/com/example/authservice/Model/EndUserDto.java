package com.example.authservice.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EndUserDto {
    private String username;
    private String password;

}
