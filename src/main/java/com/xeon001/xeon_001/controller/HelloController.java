package com.xeon001.xeon_001.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/teste")
    public String teste(){
        return "hellooooo!!!!, tudo certo ate aquiiii";
    }
}
