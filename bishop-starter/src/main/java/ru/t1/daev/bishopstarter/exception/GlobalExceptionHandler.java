package ru.t1.daev.bishopstarter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.t1.daev.bishopstarter.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommandQueueFullException.class)
    public ResponseEntity<ErrorResponse> handleQueueFull(CommandQueueFullException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse(
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Queue Overflow",
                        e.getMessage()
                ));
    }

    @ExceptionHandler({InvalidCommandException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleInvalidCommand(Exception e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid Command",
                        e.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Error",
                        "Unexpected system malfunction"
                ));
    }
}
