package com.example.ankiai.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestException extends RuntimeException{

    /**
     *
     * @param s
     * @param exceptionDetails
     */
    public RestException(String s, String exceptionDetails){
        super(s);
        log.error("Client Exception occurred: {}\nDetails:\n{}", s, exceptionDetails);
    }
}
