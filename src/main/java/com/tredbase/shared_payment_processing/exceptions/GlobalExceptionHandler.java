package com.tredbase.shared_payment_processing.exceptions;



import com.tredbase.shared_payment_processing.models.ApiResponse;
import com.tredbase.shared_payment_processing.models.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .message("Malformed JSON request")
                .status(HttpStatus.BAD_REQUEST)
                .build();
        ApiResponse response = new ApiResponse("40000", exception.getMessage(), errorDetail);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidExceptionException(MethodArgumentNotValidException exception) {
        String errorMessage = "Request validation failure. Please check your request data.";
        BindingResult result = exception.getBindingResult();
        FieldError fieldError = result.getFieldError();
        if(fieldError != null) {
            errorMessage = fieldError.getDefaultMessage();
        }
        ApiResponse apiResponse = new ApiResponse("42200", errorMessage, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleParentNotFoundException(final ParentNotFoundException exception){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        ApiResponse response = new ApiResponse("40400", exception.getMessage(), errorDetail);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleStudentNotFoundException(final StudentNotFoundException exception){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        ApiResponse response = new ApiResponse("40401", exception.getMessage(), errorDetail);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentAuthorizationException.class)
    public ResponseEntity<ApiResponse> handlePaymentAuthorizationException(final PaymentAuthorizationException exception){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_ACCEPTABLE)
                .build();
        ApiResponse response = new ApiResponse("40600", exception.getMessage(), errorDetail);
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(final UserNotFoundException exception){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
        ApiResponse response = new ApiResponse("40402", exception.getMessage(), errorDetail);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse> handleInvalidPasswordException(final InvalidPasswordException exception){
        ErrorDetail errorDetail = ErrorDetail.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        ApiResponse response = new ApiResponse("40001", exception.getMessage(), errorDetail);
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }
}
