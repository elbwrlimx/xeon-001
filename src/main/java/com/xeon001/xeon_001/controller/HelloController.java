package com.xeon001.xeon_001.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    // SEU ENDPOINT ORIGINAL - continua funcionando
    @GetMapping("/teste")
    public String teste() {
        return "Hellooooo!!!!, tudo certo ate aquiii";
    }
    
    // NOVOS ENDPOINTS PARA TESTAR JWT
    @GetMapping("/public/hello")
    public String publicHello() {
        return "aqui vc acessa sem precisar de token";
    }
    
    @GetMapping("/private/hello") 
    public String privateHello() {
        return "Hello World - Privado (precisa de token JWT)!";
    }
}