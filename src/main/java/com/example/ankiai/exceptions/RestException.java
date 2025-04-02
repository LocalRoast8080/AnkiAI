package com.example.ankiai.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestException extends RuntimeException {

    /**
     * An Exception wrapper for all RestClientExceptions.
     *
     * @param message          A message describing where the failure occurred formated 'Failed to perform spell {failure location} REST call.'.
     * @param exceptionMessage The message from the RestClientException.
     */
    public RestException(String message, String exceptionMessage) {
        super(message);
        log.error("REST Client Exception occurred: {}\nDetails:\n{}", message, exceptionMessage);
    }
}
