package com.example.weatherservice.Exception;

public class UnAuthorizedAccess extends Exception{
    public UnAuthorizedAccess(String message){
        super(message);
    }
}
