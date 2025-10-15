package com.mountblue.blogapplication.Cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configure->
                configure
                        .requestMatchers("/","/register","/post/{id}","/post/filter/**","/post/{postId}/comment").permitAll()
                        .requestMatchers("/newpost","/post/{id}/**").hasAnyRole("AUTHOR","ADMIN")
                        .requestMatchers("/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                        .formLogin(form-> form.
                                                                            loginPage("/login")
                                                                            .loginProcessingUrl("/authenticateTheUser")
                                                                            .permitAll())
                .logout(logout->logout.permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
