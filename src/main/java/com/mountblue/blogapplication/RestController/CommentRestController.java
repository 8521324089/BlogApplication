package com.mountblue.blogapplication.RestController;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentRestController {
    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<Set<Comment>> getComments(@PathVariable Long postId) {
        Set<Comment> comment = commentService.getComments(postId);
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        Comment savedComment = commentService.addComment(comment, postId);
        return savedComment != null ? ResponseEntity.ok(savedComment) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId, @PathVariable Long id,
                                                 @RequestBody Comment comment) {
        Comment savedComment = commentService.updateComment(comment, postId, id);
        return savedComment != null ? ResponseEntity.ok(savedComment) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long id) {
        if (commentService.deleteComment(id, postId))
            return ResponseEntity.ok("Comment deleted successfully");
        return ResponseEntity.notFound().build();
    }
}
