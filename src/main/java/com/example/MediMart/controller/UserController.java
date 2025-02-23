package com.example.MediMart.controller;

import com.example.MediMart.model.User;
import com.example.MediMart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return userService.loginUser(email, password);
    }
    @GetMapping("/profile/{email}")
    public Map<String, Object> getUser(@PathVariable String email) {
        Map<String, Object> userData = userService.getUserDetailsByEmail(email);

        if (userData == null) {
            throw new RuntimeException("User not found");
        }

        return userData;
    }
}
