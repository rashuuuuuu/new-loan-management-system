package com.rashmita.bankservice.authenticationController;
import com.rashmita.common.entity.User;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.security.dtos.LoginUserDto;
import com.rashmita.common.security.dtos.RegisterResponse;
import com.rashmita.common.security.dtos.RegisterUserDto;
import com.rashmita.common.security.services.AuthenticationService;
import com.rashmita.common.security.services.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtUtils;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, JwtService jwtUtils) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterUserDto registerUserDto) throws NotFoundException {
        RegisterResponse registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
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