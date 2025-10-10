package com.rashmita.systemservice.repository;
import com.rashmita.systemservice.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    User findByUsername(String username);
    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();

    boolean existsByEmail(String email);
}