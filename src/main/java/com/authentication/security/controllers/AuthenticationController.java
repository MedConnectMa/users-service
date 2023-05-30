package com.authentication.security.controllers;

import com.authentication.security.responseMessage.ResponseMsg;
import com.authentication.security.models.auth.AuthenticationRequest;
import com.authentication.security.models.auth.RegisterRequest;
import com.authentication.security.models.user.User;
import com.authentication.security.repository.UserRepository;
import com.authentication.security.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            ResponseMsg errorResponse = ResponseMsg.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .Message("Email already exist")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return ResponseEntity.ok(authenticationService.register(request));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        Optional<User> userEmail = userRepository.findByEmail(request.getEmail());
        if (userEmail.isEmpty()) {
            ResponseMsg errorResponse = ResponseMsg.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .Message("Email or password incorrect")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        User user = userEmail.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            ResponseMsg errorResponse = ResponseMsg.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .Message("Email or password incorrect")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
