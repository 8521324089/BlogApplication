package com.mountblue.blogapplication.Cofig;

import com.mountblue.blogapplication.Model.User;
import com.mountblue.blogapplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("UserName not Found" + username));

        return org.springframework.security.core.userdetails
                .User
                .withUsername(user.getEmail())
                .password("{noop}" + user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
