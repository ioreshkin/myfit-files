package com.sharko.myfit.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleExeption (UploadExeption e){
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleExeption (RuntimeException e){
        log.error(e.getMessage(), e);
        return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
    }


}
