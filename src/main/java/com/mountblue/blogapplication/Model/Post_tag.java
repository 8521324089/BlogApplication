package com.mountblue.blogapplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "post_tags")
@Getter
@NoArgsConstructor
public class Post_tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post_id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag_id;

    @CreationTimestamp
    private LocalDateTime created_at;

    private  LocalDateTime updated_at;

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Post_tag{" +
                "post_id=" + post_id +
                ", tag_id=" + tag_id +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
