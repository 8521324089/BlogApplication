package com.mountblue.blogapplication.RestController;

import com.mountblue.blogapplication.Model.User;
import com.mountblue.blogapplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        if (savedUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed. Please check your details.");
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedUser.getId());
            response.put("email", savedUser.getEmail());
            response.put("name", savedUser.getName());
            response.put("role", savedUser.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
