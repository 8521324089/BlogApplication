package com.mountblue.blogapplication.RestController;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Service.PostService;
import com.mountblue.blogapplication.Service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private final PostService postService;
    private final TagService tagService;

    public PostRestController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Post> reqPosts = postService.getAllPosts(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", reqPosts.getContent());
        response.put("authors", postService.getAllAuthor());
        response.put("tags", tagService.getAllTags());
        response.put("currPage", page);
        response.put("totalPages", reqPosts.getTotalPages());
        response.put("totalItems", reqPosts.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = Optional.ofNullable(postService.getById(id));
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post,
                                        @RequestParam(required = false) String allTag) {
        Post savedPost = postService.savePost(post, allTag);
        return savedPost != null ? ResponseEntity.ok(savedPost) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Post not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
                                           @RequestBody Post updatedPost,
                                           @RequestParam(required = false) String allTag) {
        Post existingPost = postService.findPostById(id);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }

        updatedPost.setId(id);
        Post savedPost = postService.savePost(updatedPost, allTag);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String prevKeyword,
            @RequestParam(required = false) List<String> authors,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Post> reqPosts;

        if (prevKeyword != null && !prevKeyword.isEmpty() && !prevKeyword.equals(keyword)) {
            reqPosts = postService.searchByKeyword(keyword, page, size);
        } else {
            reqPosts = postService.filterPosts(keyword, authors, tags, fromDate, toDate, sort, page, size);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("keyword", keyword);
        response.put("prevKeyword", keyword);
        response.put("selectedAuthors", authors);
        response.put("selectedTags", tags);
        response.put("fromDate", fromDate);
        response.put("toDate", toDate);
        response.put("selectedSort", sort);
        response.put("posts", reqPosts.getContent());
        response.put("authors", postService.getAllAuthor());
        response.put("tags", tagService.getAllTags());
        response.put("currPage", page);
        response.put("totalPages", reqPosts.getTotalPages());
        response.put("totalItems", reqPosts.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
