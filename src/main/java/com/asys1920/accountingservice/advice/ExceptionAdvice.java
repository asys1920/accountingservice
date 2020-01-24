package com.asys1920.accountingservice.advice;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.net.ConnectException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<String> handleValidationException(Exception ex){
        return new ResponseEntity<>(jsonFromException(ex), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConnectException.class)
    @ResponseBody
    public ResponseEntity<String> handleConnectException(Exception ex){
        return new ResponseEntity<>(jsonFromException(ex), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception ex){
        return new ResponseEntity<>(jsonFromException(ex), HttpStatus.BAD_REQUEST);
    }
    private String jsonFromException(Exception ex){
        JSONObject response = new JSONObject();
        response.put("message",ex.getMessage());
        return response.toJSONString();
    }
}
