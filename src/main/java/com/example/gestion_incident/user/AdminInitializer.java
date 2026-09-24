package com.example.gestion_incident.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@admin.com";

        // Check if the admin already exists to prevent duplicate inserts
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .firstname("Super")
                    .lastname("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123")) // Always hash the password!
                    .role(Role.SUPER_ADMIN) // Make sure ADMIN or SUPER_ADMIN exists in your Role enum
                    .build();

            userRepository.save(admin);
            System.out.println("=================================================");
            System.out.println("[BACKEND - INIT] Super Admin created successfully!");
            System.out.println("Email: " + adminEmail);
            System.out.println("=================================================");
        }
    }
}
