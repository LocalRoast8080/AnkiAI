package com.example.ankiai;

import com.example.ankiai.execptions.AiException;
import com.example.ankiai.execptions.EmptyFileException;
import com.example.ankiai.execptions.InvalidFileException;
import com.example.ankiai.execptions.SearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidFileException.class)
    public ProblemDetail handleInvalidFile(InvalidFileException e, WebRequest req){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

    }

    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handleEmptyFile(EmptyFileException e, WebRequest req){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AiException.class)
    public ProblemDetail handleAiException(AiException e, WebRequest req){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SearchException.class)
    public ProblemDetail handleSearchException(SearchException e, WebRequest req) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
