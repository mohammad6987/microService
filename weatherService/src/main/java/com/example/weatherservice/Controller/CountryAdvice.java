package com.example.weatherservice.Controller;



import com.example.weatherservice.Exception.CountryNotFoundException;
import com.example.weatherservice.Exception.UnAuthorizedAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class CountryAdvice {
    @ExceptionHandler(value = CountryNotFoundException.class)
    public ResponseEntity<Object> exception(CountryNotFoundException exception) {
        return new ResponseEntity<>("Country not found", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = UnAuthorizedAccess.class)
    public ResponseEntity<String> unauthorizedAccess(UnAuthorizedAccess unAuthorizedAccess){
        return new ResponseEntity<>("Your token doesn't work , please check the syntax or use another token" , HttpStatusCode.valueOf(403));
    }











}
