package com.kuba.GymTrackerAPI.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MultipleArgumentErrorObject> handleNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField() + "Message";
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        MultipleArgumentErrorObject multipleArgumentErrorObject = new MultipleArgumentErrorObject(HttpStatus.BAD_REQUEST.value(), errors, new Date());

        return new ResponseEntity<MultipleArgumentErrorObject>(multipleArgumentErrorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorObject> handleBadRequest(BadRequestException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date());

        return new ResponseEntity<ErrorObject>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorObject> handleAlreadyExists(AlreadyExistsException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.CONFLICT.value(), ex.getMessage(), new Date());

        return new ResponseEntity<ErrorObject>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorObject> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), new Date());

        return new ResponseEntity<ErrorObject>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Void> handleJwtException(JwtException ex) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
