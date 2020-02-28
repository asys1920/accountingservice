package com.asys1920.service.advice;

import com.asys1920.service.exceptions.ValidationException;
import lombok.Data;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({RepositoryConstraintViolationException.class})
    public ResponseEntity<ErrorMessage> handleRepositoryConstraintViolationException(
            RepositoryConstraintViolationException ex) {
        List<String> errors = ex.getErrors().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        LOG.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorMessage(errors));
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, NoSuchElementException.class})
    @ResponseBody
    public ResponseEntity<JSONObject> handleNoSuchEntity(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        return new ResponseEntity<>(jsonFromException(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<JSONObject> handleValidationException(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        return new ResponseEntity<>(jsonFromException(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseBody
    public ResponseEntity<JSONObject> handleAdapterNotConnected(Exception ex) {
        LOG.error(ex.getMessage(), ex);
        JSONObject response = jsonFromException(ex);
        String newMessage = "Userservice is currently unavailable, please try again later.";
        response.put("message", newMessage);
        return new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
    }


    @Data
    private static class ErrorMessage {
        private final String cause = "VALIDATION FAILED";
        private List<String> description;

        public ErrorMessage(List<String> description) {
            this.description = description;
        }
    }

    private JSONObject jsonFromException(Exception ex) {
        JSONObject response = new JSONObject();
        response.put("message", ex.getMessage());
        return response;
    }
}
