package com.kuba.GymTrackerAPI.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MultipleArgumentErrorObject> handleNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField() + "Message";
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        MultipleArgumentErrorObject multipleArgumentErrorObject = new MultipleArgumentErrorObject(HttpStatus.BAD_REQUEST.value(), errors, new Date());

        return new ResponseEntity<>(multipleArgumentErrorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorObject> handleBadRequest(BadRequestException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorObject> handleAlreadyExists(AlreadyExistsException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.CONFLICT.value(), ex.getMessage(), new Date());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorObject> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), new Date());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Void> handleJwtException(JwtException ex) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorObject> handleNotFoundException(NotFoundException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new Date());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.BAD_REQUEST.value(), "Chyba typu v URL adrese, prosím zkontrolujte URL adresu!", new Date());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorObject> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorObject error = new ErrorObject(HttpStatus.BAD_REQUEST.value(), "Nesprávný datový typ!", new Date());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
