package com.rashmita.systemservice.security.services;
import com.rashmita.systemservice.constants.StatusConstants;
import com.rashmita.systemservice.entity.Status;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.repository.AccessGroupRepository;
import com.rashmita.systemservice.repository.RoleRepository;
import com.rashmita.systemservice.repository.StatusRepository;
import com.rashmita.systemservice.repository.UserRepository;
import com.rashmita.systemservice.security.dtos.LoginUserDto;
import com.rashmita.systemservice.security.dtos.RegisterResponse;
import com.rashmita.systemservice.security.dtos.RegisterUserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
private final AccessGroupRepository accessGroupRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, AccessGroupRepository accessGroupRepository, RoleRepository roleRepository, StatusRepository statusRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessGroupRepository = accessGroupRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
    }

    public RegisterResponse signup(RegisterUserDto input) throws NotFoundException {
        var accessGroupName = input.getAccessGroup().getName();
        var accessGroup = accessGroupRepository.findByName(accessGroupName)
                .orElseThrow(() -> new NotFoundException("Access group not found: " + accessGroupName));

        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setUsername(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setAccessGroup(accessGroup);
        Status createdStatus = statusRepository.getStatusByName(StatusConstants.CREATED.getName());
        user.setStatus(createdStatus);
        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setAccessGroup(savedUser.getAccessGroup().getName());
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

        return userRepository.findByEmail(input.getEmail()).orElseThrow();

    }
}