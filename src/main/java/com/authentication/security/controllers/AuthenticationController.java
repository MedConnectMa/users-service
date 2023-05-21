package com.authentication.security.controllers;

import com.authentication.security.models.auth.AuthenticationRequest;
import com.authentication.security.models.auth.AuthenticationResponse;
import com.authentication.security.models.auth.RegisterRequest;
import com.authentication.security.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
