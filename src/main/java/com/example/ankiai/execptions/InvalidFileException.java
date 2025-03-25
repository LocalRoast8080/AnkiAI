package com.example.ankiai.execptions;

public class InvalidFileException extends RuntimeException{

    public InvalidFileException(String s){
        super(s);
    }
}
