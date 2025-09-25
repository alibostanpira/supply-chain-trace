package dev.abpira.sct.exception;

import org.springframework.lang.NonNull;
import dev.abpira.sct.dto.ErrorResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> {
            String fieldName = error.getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductAlreadyExistException(ProductAlreadyExistsException ex,
                                                                               WebRequest webRequest) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductNotFoundException(ProductNotFoundException ex,
                                                                           WebRequest webRequest) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(HttpStatus.NOT_FOUND)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidMovementException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidMovementException(InvalidMovementException ex,
                                                                           WebRequest webRequest) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImmutableMovementException.class)
    public ResponseEntity<ErrorResponseDTO> handleImmutableMovementException(ImmutableMovementException ex,
                                                                             WebRequest webRequest) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorCode(HttpStatus.CONFLICT)
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .build(), HttpStatus.CONFLICT);
    }
}
