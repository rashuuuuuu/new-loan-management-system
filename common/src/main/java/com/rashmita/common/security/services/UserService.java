package com.rashmita.common.security.services;

import com.rashmita.common.entity.User;
import com.rashmita.common.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
//    public User loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            return (User) org.springframework.security.core.userdetails.User.builder()
//                    .username(user.getUsername())
//                    .password(user.getPassword())
//                    .roles(user.getRole().toArray(new String[0]))
//                    .build();
//        }
//        throw new UsernameNotFoundException("User not found with username: " + username);
//    }
}