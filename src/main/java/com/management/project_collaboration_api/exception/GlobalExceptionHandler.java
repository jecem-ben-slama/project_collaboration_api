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

    // Handle Foreign Key Violations and Database Constraints
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = "Cannot delete this item because it is currently being used by another part of the system.";

        // Optional: You can check if the message contains specific table names to be
        // more precise
        if (ex.getMessage().contains("affectation")) {
            message = "Cannot delete this project while users are still assigned to it.";
        } else if (ex.getMessage().contains("note")) {
            message = "Cannot delete this project because it has existing notes.";
        }

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Database Constraint Violation",
                message);

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Handle "Resource Not Found" (Optional but recommended)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error Occurred",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}