package com.mountblue.blogapplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "tags")
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @CreationTimestamp
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "tag_id")
    private List<Post_tag> post;

    public Tag(String name, LocalDateTime updated_at, List<Post_tag> post) {
        this.name = name;
        this.updated_at = updated_at;
        this.post = post;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setPost(List<Post_tag> post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", post=" + post +
                '}';
    }
}
