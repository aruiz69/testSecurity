package com.example.demo.controller;

import com.example.demo.config.CustomCsrfTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrf")
public class CsrfController {

    @GetMapping("/token")
    public String generateCsrfToken(CustomCsrfTokenRepository customCsrfTokenRepository,
                                    HttpServletRequest request, HttpServletResponse response) {
        var csrfToken = customCsrfTokenRepository.generateToken(request);
        customCsrfTokenRepository.saveToken(csrfToken, request, response);
        return csrfToken.getToken();
    }
}
