package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.User;
import com.mountblue.blogapplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setRole("AUTHOR");
        if(userRepository.findByEmail(user.getEmail()).isEmpty())
            return userRepository.save(user);
        return new User();
    }

    public Optional getByUsername(String username){
        return userRepository.findByEmail(username);
    }
}
