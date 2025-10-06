package com.rashmita.systemservice.security.services;
import com.rashmita.systemservice.constants.StatusConstants;
import com.rashmita.systemservice.entity.Roles;
import com.rashmita.systemservice.repository.StatusRepository;
import com.rashmita.systemservice.security.dtos.LoginUserDto;
import com.rashmita.systemservice.security.dtos.RegisterResponse;
import com.rashmita.systemservice.security.dtos.RegisterUserDto;
import com.rashmita.systemservice.constants.RoleEnum;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.repository.RoleRepository;
import com.rashmita.systemservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository, StatusRepository statusRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository
    ) {
        this.statusRepository = statusRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public RegisterResponse signup(RegisterUserDto input) {
        Optional<Roles> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (!optionalRole.isPresent()) {
            throw new RuntimeException("User role not found");
        }

        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());
        user.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));
        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setCreatedAt(savedUser.getCreatedAt());
        return response;
    }
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}