package com.example.demo.Exceptions;



import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

 
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", ex.getBindingResult().getFieldErrors()
                .stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        return new ResponseEntity<>(Map.of("message", "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
