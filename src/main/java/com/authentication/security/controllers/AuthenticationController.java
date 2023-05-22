package com.authentication.security.controllers;

import com.authentication.security.error.ErrorResponseMsg;
import com.authentication.security.models.auth.AuthenticationRequest;
import com.authentication.security.models.auth.AuthenticationResponse;
import com.authentication.security.models.auth.RegisterRequest;
import com.authentication.security.models.user.User;
import com.authentication.security.repository.UserRepository;
import com.authentication.security.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            ErrorResponseMsg errorResponse = ErrorResponseMsg.builder()
                    .errorMessage("Email already exist")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        Optional<User> userEmail = userRepository.findByEmail(request.getEmail());
        if (userEmail.isEmpty()) {
            ErrorResponseMsg errorResponse = ErrorResponseMsg.builder()
                    .errorMessage("Email or password incorrect")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
