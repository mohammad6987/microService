package com.example.authservice.Exception;

public class UnAuthorizedAccess extends Exception{
    public UnAuthorizedAccess(String message){
        super(message);
    }
}
