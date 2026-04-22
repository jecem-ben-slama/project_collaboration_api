package com.management.project_collaboration_api.exception;

import com.management.project_collaboration_api.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST; // Default to 400
        String title = "Error Occurred";

        // Logic to catch the "Nice Error" without a custom class
        if (message != null && message.contains("already assigned")) {
            status = HttpStatus.CONFLICT; // Change to 409
            title = "Conflict Error";
        }

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                title,
                message);

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = "Database constraint violation.";
        String errorMessage = ex.getMessage().toLowerCase();

        if (errorMessage.contains("affectation")) {
            message = "This operation failed because the user or project is still linked to active assignments.";
        }

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Database Constraint Violation",
                message);

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}