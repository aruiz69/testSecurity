package com.example.demo.controller;

import com.example.demo.modelo.DepositDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class EndPoints {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    /**
     * Metodo para obtener el balance de la cuenta
     * @return balance de la cuenta
     */
    @GetMapping("/balance")
    public String balance() {
        return  """ 
                  {
                      "name": "John Doe",
                      "accountNumber": "123456789",
                      "balance": 1000.00
                  }
                """;
    }


    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody DepositDto depositDto) {
        return ResponseEntity.ok("Depósito realizado con éxito para la cuenta " + depositDto.getAccountNumber() + " a nombre de " + depositDto.getCustomerName() + " por un monto de " + depositDto.getDepositAmount());
    }



}
