package com.rashmita.systemservice.repository;
import com.rashmita.systemservice.entity.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Long> {
//    Optional<Roles> findByName(RoleEnum name);

    Roles findByName(String name);
    @Query("SELECT r FROM Roles r")
    List<Roles> getAllRoles();
    List<Roles> findByNameIn(List<String> names);

    boolean existsByName(String name);
}
