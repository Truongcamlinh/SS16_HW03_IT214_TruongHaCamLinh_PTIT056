package com.tiki.inventory.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(InventoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, Object> notFound(RuntimeException error) { return body(404, error.getMessage()); }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, Object> badRequest(Exception error) {
        String message = error instanceof MethodArgumentNotValidException validation
                ? validation.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                : error.getMessage();
        return body(400, message);
    }

    private Map<String, Object> body(int status, String message) {
        return Map.of("timestamp", Instant.now().toString(), "status", status, "message", message);
    }
}
