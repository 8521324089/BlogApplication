package com.mountblue.blogapplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Comments")
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    @Column(columnDefinition = "text")
    private String comment;
    @CreationTimestamp
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post_id;

    public Comment(String name, String email, String comment, LocalDateTime updated_at, Post post_id) {
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.updated_at = updated_at;
        this.post_id = post_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setPost_id(Post post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", comment='" + comment + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", post_id=" + post_id +
                '}';
    }
}
