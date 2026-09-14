package com.example.gestion_incident.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    @GetMapping("/techniciens")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<User>> getTechniciens() {
//        return ResponseEntity.ok(userService.getTechniciens());
//    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN','USER','TECHNICIEN')")
    public ResponseEntity<User> updateProfile(Principal principal, @RequestBody User userDetails) {
        try {
            // Spring Security extrait l'email (le "subject") du JWT connecté
            String userEmail = principal.getName();

            // Appel de votre service mis à jour pour chercher par email
            User updatedUser = userService.updateUserByEmail(userEmail, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
