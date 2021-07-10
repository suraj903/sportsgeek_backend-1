package com.project.sportsgeek.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/error")
public class AppErrorController implements ErrorController {

    @GetMapping
    public ResponseEntity<String> onError(){
        return ResponseEntity.ok("<h1>SportsGeek Application</h1><br/><h3>Sorry, you are unauthorized to access.</h3>");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}