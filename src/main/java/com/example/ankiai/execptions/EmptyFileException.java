package com.example.ankiai.execptions;

public class EmptyFileException extends RuntimeException{

    public EmptyFileException(String s){
        super(s);
    }
}
