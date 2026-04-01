package com.klu.demoprojrect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klu.demoprojrect.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
        String username = creds.get("username");
        String password = creds.get("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username and password are required"));
        }

        // Trim inputs
        String inputUsername = username.trim();
        String inputPassword = password.trim();

        // Fetch user using trimmed username
        Optional<User> opt = userRepository.findByUsername(inputUsername);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
        }

        User user = opt.get();

        // Debug logs
        System.out.println("DB username: " + user.getUsername());
        System.out.println("DB password: " + user.getPassword());
        System.out.println("Input username: " + inputUsername);
        System.out.println("Input password: " + inputPassword);

        String dbPass = user.getPassword() == null ? "" : user.getPassword().trim();
        if (!dbPass.equals(inputPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Login success");
        Map<String, Object> u = new HashMap<>();
        u.put("id", user.getId());
        u.put("username", user.getUsername());
        u.put("role", user.getRole());
        resp.put("user", u);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");
        String role = body.get("role");

        if (username == null || password == null || role == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields required"));
        }

        String trimmedUsername = username.trim();
        String trimmedPassword = password.trim();
        String trimmedRole = role.trim();

        if (trimmedUsername.isEmpty() || trimmedPassword.isEmpty() || trimmedRole.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields required"));
        }

        if (userRepository.findByUsername(trimmedUsername).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already exists"));
        }

        User u = new User();
        u.setUsername(trimmedUsername);
        u.setPassword(trimmedPassword);
        u.setRole(trimmedRole);

        userRepository.save(u);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully"));
    }
}