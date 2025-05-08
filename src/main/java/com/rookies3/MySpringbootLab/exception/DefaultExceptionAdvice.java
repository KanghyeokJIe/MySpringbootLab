package com.rookies3.MySpringbootLab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusinessException(BusinessException ex) {
        ErrorObject error = new ErrorObject("BusinessException", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // 404 응답
    }
}
