package com.cardealership;

import com.cardealership.entity.User;
import com.cardealership.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class CarDealershipApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CarDealershipApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        loadEnv();
        SpringApplication.run(CarDealershipApplication.class, args);
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

    private static void loadEnv() {
        Path envPath = Paths.get(".env");
        if (!Files.exists(envPath)) {
            envPath = Paths.get("backend/.env");
        }

        if (Files.exists(envPath)) {
            try {
                List<String> lines = Files.readAllLines(envPath);
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not load environment from .env file: " + e.getMessage());
            }
        }
    }
}
