package com.clearsolutions.controller;

import com.clearsolutions.exeption.UserErrorResponse;
import com.clearsolutions.exeption.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling various exceptions that may occur in the application.
 */
@ControllerAdvice
public class UserExceptionHandler {

    /**
     * Handles DateTimeException when parsing date/time strings.
     * @param ex The DateTimeException instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<UserErrorResponse> handleDateTimeParseException(DateTimeException ex) {
        // Create a custom error response
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid date format : " +  ex.getMessage(), new Timestamp(System.currentTimeMillis()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UserNotFoundException.
     * @param exception The UserNotFoundException instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException exception) {
        // Create a custom error response
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), new Timestamp(System.currentTimeMillis()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles generic exceptions.
     * @param exc The Exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(Exception exc) {
        // Create a custom error response
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(), new Timestamp(System.currentTimeMillis()).toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException, which occurs when method arguments fail validation.
     * @param exc The MethodArgumentNotValidException instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler()
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        StringBuilder strBuilder = new StringBuilder();

        // Iterate through all validation errors and append them to the StringBuilder
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();
            } catch (ClassCastException ex) {
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();
            strBuilder.append(fieldName).append(" : ").append(message).append(" \n ");
        });

        // Create a map of error messages
        return new ResponseEntity<>(createErrorMap(strBuilder.toString()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Create a map containing the error message.
     * @param message The error message.
     * @return Map containing the error message.
     */
    public Map<String, String> createErrorMap(String message) {
        Map<String, String> errorMsg = new HashMap<>();
        errorMsg.put("message", message);
        return errorMsg;
    }
}
