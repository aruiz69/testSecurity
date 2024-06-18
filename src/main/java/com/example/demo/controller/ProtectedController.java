package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {


    private final CsrfTokenRepository csrfTokenRepository;

    public ProtectedController(CsrfTokenRepository csrfTokenRepository) {
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @PostMapping("/protected-resource")
    public String protectedResource(
            @RequestHeader(value = "X-XSRF-TOKEN", required = false) String csrfToken,
            HttpServletRequest request) {

        CsrfToken storedToken = csrfTokenRepository.loadToken(request);
        if (storedToken != null && storedToken.getToken() != null && storedToken.getToken().equals(csrfToken)) {
            // handle protected resource request
            return "Protected resource accessed";
        } else {
            // handle CSRF attack
            return "CSRF attack detected";
        }

    }

}
