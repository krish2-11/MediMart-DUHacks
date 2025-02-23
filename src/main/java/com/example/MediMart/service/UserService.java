package com.example.MediMart.service;

import com.example.MediMart.model.User;
import com.example.MediMart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists.";
        }
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "Check your email!";
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return "Check your password!";
        }

        return "Login successful!";
    }

    public Map<String, Object> getUserDetailsByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);  // Use existing method

        if (userOptional.isEmpty()) {
            return null;  // Return null if user not found
        }

        User user = userOptional.get();

        // Exclude password and email
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("store_name", user.getStoreName());
        userData.put("location", user.getLocation());
        userData.put("email",user.getEmail());

        return userData;
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}
