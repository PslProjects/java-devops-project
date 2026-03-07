package com.scada.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scada.entities.User;
import com.scada.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public User addUser(String username, String password) {
    	  if (repo.existsByUsername(username)) {
              throw new RuntimeException("Username already exists");
          }

        String encodedPass = encoder.encode(password);
        User user = new User(username, encodedPass);
        return repo.save(user);
    }
    public User addAdmin(String username, String password) {
    	  if (repo.existsByUsername(username)) {
              throw new RuntimeException("Username already exists");
          }

        String encodedPass = encoder.encode(password);
        User admin = new User(username, encodedPass);
        admin.setRole("ADMIN");
        return repo.save(admin);
    }

    public User validateUser(String username, String password) {
    	System.err.println("Validating user: " + username);
    	System.err.println("Provided password: " + password);

        User user = repo.findByUsername(username)
                .orElse(null);
        
        if (user == null) return null;

        boolean match = encoder.matches(password, user.getPassword());
        return match ? user : null;
    }
    public List<User> getAllUsers() {
        return repo.findAll();
    }

}
