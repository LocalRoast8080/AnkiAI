package com.example.ankiai.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteCardController {

    @GetMapping("/h")
    public String test(){
        return "Hello";
    }
}
