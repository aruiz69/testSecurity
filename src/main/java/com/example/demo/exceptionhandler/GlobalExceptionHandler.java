package com.example.demo.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleValidationExceptions(
            Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException exArg) {

            exArg.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((org.springframework.validation.FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            body.put("timestamp", LocalDateTime.now());
            body.put("errorId", UUID.randomUUID());
            body.put("errors", errors);

            logger.error("Validation MethodArgumentNotValidException failed: {}", body);

            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        } else if ( ex instanceof HttpMessageNotReadableException exHttp) {
            body.put("timestamp", LocalDateTime.now());
            body.put("errorId", UUID.randomUUID());
            body.put("errors", exHttp.getMessage());

            logger.error("Validation HttpMessageNotReadableException failed: {}", body);

            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        } else {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}