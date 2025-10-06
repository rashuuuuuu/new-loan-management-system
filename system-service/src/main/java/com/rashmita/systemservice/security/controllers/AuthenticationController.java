package com.rashmita.systemservice.security.controllers;
import com.rashmita.systemservice.security.dtos.LoginUserDto;
import com.rashmita.systemservice.security.dtos.RegisterResponse;
import com.rashmita.systemservice.security.dtos.RegisterUserDto;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.security.services.AuthenticationService;
import com.rashmita.systemservice.security.services.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/auth")
@RestController

public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtUtils;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,JwtService jwtUtils) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
      RegisterResponse registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok("signup successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        // Generate JWT
        String jwtToken = jwtService.generateToken(authenticatedUser);

        // Set token in HTTP header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // Response body with success message only
        Map<String, String> body = Map.of(
                "message", "Login successful"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

}