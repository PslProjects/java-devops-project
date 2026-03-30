package com.scada.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scada.entities.LoginRequest;
import com.scada.entities.User;
import com.scada.exception.InvalidCredentialsException;
import com.scada.service.UserService;
import com.scada.sess.AdminSessionTracker;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"https://scada.pratikshat.com", "http://198.7.114.147:8888", "http://207.180.233.141:8888", "http://localhost:8888", "http://localhost:4200"})
public class AuthController {

    @Autowired
    private UserService service;

    // --------- SIGNUP ----------
    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody LoginRequest req) {

        try {
            service.addUser(req.getUsername(), req.getPassword());
            return ResponseEntity.ok(Map.of("message", "User added successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdmin(@RequestBody LoginRequest req) {

        try {
            service.addAdmin(req.getUsername(), req.getPassword());
            return ResponseEntity.ok(Map.of("message", "Admin added successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // --------- LOGIN ----------
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) throws InvalidCredentialsException {

        User user = service.validateUser(req.getUsername(), req.getPassword());

        if (user != null) {
            return Map.of(
                "token", "LOGGED_" + user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()   // ★ MUST ADD THIS ★
            );
        }

        throw new InvalidCredentialsException("Invalid credentials");
    }
  @PostMapping("/admin/login")
public Map<String, String> adminLogin(@RequestBody LoginRequest req) throws InvalidCredentialsException {

    User user = service.validateUser(req.getUsername(), req.getPassword());

    if (user != null && "ADMIN".equals(user.getRole())) {

        // ⭐ FIX: Set active admin globally (overwrite old admin)
        AdminSessionTracker.setCurrentAdmin(user.getUsername());

        return Map.of(
            "token", "ADMIN_" + user.getId(),
            "username", user.getUsername(),
            "role", "ADMIN"
        );
    }

    throw new InvalidCredentialsException("Admin credentials required");
}
  @PostMapping("/admin/authorize")
  public ResponseEntity<?> authorizeAdmin(@RequestBody Map<String, String> req) {

      String username = req.get("username");
      String password = req.get("password");
      String masterPin = req.get("masterPin");

      // ✅ Master PIN
      if (!"999999".equals(masterPin)) {
          return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("message", "Invalid Master PIN"));
      }

      // ✅ Validate admin credentials
      User admin = service.validateUser(username, password);

      if (admin == null || !"ADMIN".equals(admin.getRole())) {
          return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body(Map.of("message", "Admin authentication failed"));
      }

      return ResponseEntity.ok(Map.of("message", "Authorized"));
  }
  @GetMapping("/admin/users")
  public ResponseEntity<?> getAllUsers() {

      List<User> users = service.getAllUsers(); // we'll add this method

      List<String> usernames = users.stream()
              .filter(u -> "USER".equals(u.getRole()))
              .map(User::getUsername)
              .collect(Collectors.toList());

      return ResponseEntity.ok(usernames);
  }




}

