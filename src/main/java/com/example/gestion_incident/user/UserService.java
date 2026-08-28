package com.example.gestion_incident.user;



import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User updateUserByEmail(String email, User userDetails) {
        // 1. On cherche l'utilisateur unique en BDD
        return userRepository.findByEmail(email).map(user -> {
            // 2. Mise à jour des informations textuelles si présentes
            if (userDetails.getFirstname() != null && !userDetails.getFirstname().trim().isEmpty()) {
                user.setFirstname(userDetails.getFirstname().trim());
            }
            if (userDetails.getLastname() != null && !userDetails.getLastname().trim().isEmpty()) {
                user.setLastname(userDetails.getLastname().trim());
            }
            if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
                user.setEmail(userDetails.getEmail().trim());
            }
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }

            // 3. Mise à jour directe du mot de passe sur l'objet Hibernate
            if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword().trim()));
            }

            // 4. On sauvegarde l'objet complet une seule fois proprement
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + email));
    }


    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id : " + id);
        }
        userRepository.deleteById(id);
    }
}

