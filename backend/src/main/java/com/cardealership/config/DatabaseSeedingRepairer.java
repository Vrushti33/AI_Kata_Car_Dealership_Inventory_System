package com.cardealership.config;

import com.cardealership.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeedingRepairer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeedingRepairer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("[STARTUP] Checking and repairing seeded user passwords...");
        
        userRepository.findByEmail("admin@cardealership.com").ifPresent(user -> {
            user.setPassword(passwordEncoder.encode("Admin@123"));
            userRepository.save(user);
            System.out.println("[STARTUP] Repaired password for admin@cardealership.com");
        });

        String[] standardEmails = {
            "mater_fan@radiator.com",
            "sally_carrera@route66.com",
            "doc_hudson_legacy@classic.com"
        };

        for (String email : standardEmails) {
            userRepository.findByEmail(email).ifPresent(user -> {
                user.setPassword(passwordEncoder.encode("User@123"));
                userRepository.save(user);
                System.out.println("[STARTUP] Repaired password for " + email);
            });
        }
    }
}
