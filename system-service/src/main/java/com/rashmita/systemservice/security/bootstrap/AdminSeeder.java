package com.rashmita.systemservice.security.bootstrap;
import com.rashmita.systemservice.constants.StatusConstants;
import com.rashmita.systemservice.entity.Roles;
import com.rashmita.systemservice.repository.StatusRepository;
import com.rashmita.systemservice.security.dtos.RegisterUserDto;
import com.rashmita.systemservice.constants.RoleEnum;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.repository.RoleRepository;
import com.rashmita.systemservice.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final StatusRepository statusRepository;


    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder, StatusRepository statusRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.statusRepository = statusRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setFullName("superadmin");
        userDto.setEmail("superadmin@email.com");
        userDto.setPassword("123456");

        Optional<Roles> optionalRole = roleRepository.findByName(RoleEnum.SUPERADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());
        user.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));

        userRepository.save(user);
    }
}