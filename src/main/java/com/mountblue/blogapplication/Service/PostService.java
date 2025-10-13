package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;

    @Autowired
    private PostService(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Post savePost(Post post, String tags) {
        String content = post.getContent();
        if (!content.isEmpty()) {
            String excerpt = content.trim().substring(0, Math.min(content.trim().length(), 300));
            post.setExcerpt(excerpt);
        }
        post.setTags(tagService.addTag(tags));
        if (post.getId() != null) {
            Post savedPost = postRepository.findById(post.getId()).get();
            post.setAuthor(savedPost.getAuthor());
            post.setComments(savedPost.getComments());
            post.setIs_published(savedPost.getIs_published());
            post.setPublishedAt(savedPost.getPublishedAt());
        }
        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size) {
        if (size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable);
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElse(new Post());
    }

    public Set<String> getAllAuthor() {
        return new HashSet<>(postRepository.findAllAuthors());
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post getById(Long id) {
        return postRepository.findById(id).orElse(new Post());
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElse(new Post());
    }

    public Page<Post> searchByKeyword(String keyword, int page, int size) {
        if (size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.searchByKeyword(pageable, keyword);
    }

    public Page<Post> filterPosts(String keyword, List<String> authors, List<String> tags, LocalDate fromDate, LocalDate toDate, String sort, int page, int size) {
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable;
        if (sort != null && sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        }

        boolean hasAuthors = authors != null && !authors.isEmpty();
        boolean hasTags = tags != null && !tags.isEmpty();

        authors = authors.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        tags = tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return postRepository.searchFilterSort(keyword, authors, tags, fromDate, toDate, hasAuthors, hasTags, (long) tags.size(), (long) authors.size(), pageable);
    }
}
