package com.mountblue.blogapplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL,orphanRemoval = true)
    Set<Post> posts;
}
