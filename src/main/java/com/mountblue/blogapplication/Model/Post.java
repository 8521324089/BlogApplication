package com.mountblue.blogapplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity(name = "posts")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(columnDefinition = "text")
    private String excerpt;
    @Column(columnDefinition = "text")
    private String content;
    private String author;
    private LocalDateTime published_at;
    private Boolean is_published;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

    public Post(String title, String excerpt, String content, String author, LocalDateTime published_at, Boolean is_published, Set<Tag> tags, List<Comment> comment) {
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.author = author;
        this.published_at = published_at;
        this.is_published = is_published;
        this.tags = tags;
        this.comment = comment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublished_at(LocalDateTime published_at) {
        this.published_at = published_at;
    }

    public void setIs_published(Boolean is_published) {
        this.is_published = is_published;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", published_at=" + published_at +
                ", is_published=" + is_published +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                ", comment=" + comment +
                '}';
    }
}
