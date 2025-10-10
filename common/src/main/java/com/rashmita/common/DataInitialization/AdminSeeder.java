//package com.rashmita.systemservice.DataInitialization;
//import com.rashmita.systemservice.constants.StatusConstants;
//import com.rashmita.systemservice.entity.AccessGroup;
//import com.rashmita.systemservice.entity.Roles;
//import com.rashmita.systemservice.entity.Status;
//import com.rashmita.systemservice.entity.User;
//import com.rashmita.systemservice.repository.AccessGroupRepository;
//import com.rashmita.systemservice.repository.RoleRepository;
//import com.rashmita.systemservice.repository.StatusRepository;
//import com.rashmita.systemservice.repository.UserRepository;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//@Component
//public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
//
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final StatusRepository statusRepository;
//    private final AccessGroupRepository accessGroupRepository;
//
//    public AdminSeeder(
//            RoleRepository roleRepository,
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder,
//            StatusRepository statusRepository, AccessGroupRepository accessGroupRepository
//    ) {
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.statusRepository = statusRepository;
//        this.accessGroupRepository = accessGroupRepository;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        this.createSuperAdministrator();
//    }
//
//    private void createSuperAdministrator() {
//        AccessGroup superAdminGroup = accessGroupRepository.findByName("SUPERADMIN_GROUP");
//        if (superAdminGroup == null) {
//            superAdminGroup = new AccessGroup();
//            superAdminGroup.setName("SUPERADMIN_GROUP");
//            superAdminGroup.setDescription("Group for Super Administrators");
//            accessGroupRepository.save(superAdminGroup);
//        }
//
//        Roles superAdminRole = roleRepository.findByName(("SUPERADMIN"));
//        if (superAdminRole == null) {
//            throw new RuntimeException("SUPERADMIN role not found");
//        }
//
//        User admin = new User();
//        admin.setFullName("Super Administrator");
//        admin.setEmail("admin@example.com");
//        admin.setPassword(passwordEncoder.encode("admin123"));
//        admin.setRole(superAdminRole);
//        admin.setStatus(
//                statusRepository.findByName(StatusConstants.CREATED.getName())
//                        .orElseGet(() -> {
//                            // If Status doesn't exist, create and save it
//                            Status s = new Status();
//                            s.setName(StatusConstants.CREATED.getName());
//                            s.setDescription(""); // optional, add description if needed
//                            s.setIcon("");        // optional, add icon if needed
//                            return statusRepository.save(s);
//                        })
//        ); admin.setAccessGroup(superAdminGroup);
//        userRepository.save(admin);
//    }
//
//}

package com.rashmita.common.DataInitialization;
import com.rashmita.common.config.SuperAdminConfiguration;
import com.rashmita.common.entity.AccessGroup;
import com.rashmita.common.entity.Status;
import com.rashmita.common.entity.User;
import com.rashmita.common.model.SuperAdminDto;
import com.rashmita.common.repository.AccessGroupRepository;
import com.rashmita.common.repository.StatusRepository;
import com.rashmita.common.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn("dataInitializer")
public class AdminSeeder {

    private final SuperAdminConfiguration superAdminConfiguration;
    private final UserRepository adminRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    private void populateAdmin() {
        List<SuperAdminDto> admins = superAdminConfiguration.getSuperAdmins();

        admins.forEach(adminDto -> {
            // Check existence by email directly
            if (adminRepository.existsByEmail(adminDto.getEmail())) {
                log.info("⚠️ Admin '{}' already exists, skipping.", adminDto.getEmail());
                return;
            }

            saveAdmin(adminDto);
        });

        log.info("✅ Super Admin(s) persisted successfully without duplicates.");
    }

    private void saveAdmin(SuperAdminDto adminDto) {
        User admin = new User();
        admin.setUsername(adminDto.getEmail());  // Username = encrypted email
        admin.setEmail(adminDto.getEmail());     // Email = encrypted email
        admin.setPassword(bCryptPasswordEncoder.encode(adminDto.getPassword()));

        AccessGroup accessGroup = accessGroupRepository.findByName("SUPER_ADMIN")
                .orElseThrow(() -> new RuntimeException("Access group SUPER_ADMIN not found"));
        admin.setAccessGroup(accessGroup);

        Status status = statusRepository.getByName("CREATED")
                .orElseThrow(() -> new RuntimeException("Status 'CREATED' not found"));
        admin.setStatus(status);

        adminRepository.save(admin);
        log.info("✅ Admin '{}' created.", adminDto.getEmail());
    }
}