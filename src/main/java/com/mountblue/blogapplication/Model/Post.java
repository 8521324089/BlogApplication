package com.mountblue.blogapplication.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "posts")
@Getter
@NoArgsConstructor
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
    @CreationTimestamp
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "post_id")
    private List<Post_tag> tag;

    @OneToMany(mappedBy = "post_id")
    private List<Comment> comment;

    public Post(String title, String excerpt, String content, String author, LocalDateTime published_at, Boolean is_published, LocalDateTime updated_at, List<Post_tag> tag, List<Comment> comment) {
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.author = author;
        this.published_at = published_at;
        this.is_published = is_published;
        this.updated_at = updated_at;
        this.tag = tag;
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

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setTag(List<Post_tag> tag) {
        this.tag = tag;
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
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", tag=" + tag +
                ", comment=" + comment +
                '}';
    }
}
