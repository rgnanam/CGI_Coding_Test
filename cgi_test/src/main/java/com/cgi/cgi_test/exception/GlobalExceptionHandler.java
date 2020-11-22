package com.cgi.cgi_test.exception;



import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cgi.cgi_test.common.Constants;

import static com.cgi.cgi_test.common.Constants.UNKNOWN_EXCEPTION_CD;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(final Exception exception) {
        return new ResponseEntity<Object>(
                new ExceptionInfo(UNKNOWN_EXCEPTION_CD, exception.getMessage()),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler({CGIBankOperationException.class})
    public ResponseEntity<Object> handleCGIException(final CGIBankOperationException exception) {

        return new ResponseEntity<Object>(
                new ExceptionInfo(exception.getErrorCode(), exception.getErrorMsg()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Errors handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

   
   @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Errors handleConstraintViolationException(final ConstraintViolationException e) {
        return processConstraintViolationErrors(e);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException runtimeException) {

        return new ResponseEntity<Object>(
                new ExceptionInfo(UNKNOWN_EXCEPTION_CD, runtimeException.getMessage()),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Errors processFieldErrors(List<FieldError> fieldErrors) {
        Errors errors = new Errors();
        errors.setCode(Constants.VALIDATION_ERROR_CD);
        errors.setDescription(Constants.VALIDATION_ERRORS);

        for (FieldError error : fieldErrors) {
            Violation violation = new Violation();
            violation.setField(error.getField());
            violation.setMessage(error.getDefaultMessage());
            errors.getValidations().add(violation);
        }

        return errors;
    }

   private Errors processConstraintViolationErrors(final ConstraintViolationException e) {
        ConstraintViolation<?> violationException = e.getConstraintViolations().stream().findFirst().get();
        // get the last node of the violation
        String fieldPath= "";
        for (Path.Node node : violationException.getPropertyPath()) {
            fieldPath = node.getName();
        }
        Violation violation = new Violation();
        violation.setField(fieldPath);
        violation.setMessage(violationException.getMessage());
        Errors errors = new Errors();
        errors.setCode(Constants.VALIDATION_ERROR_CD);
        errors.setDescription(Constants.VALIDATION_ERRORS);
        errors.getValidations().add(violation);
        return errors;
    }

}