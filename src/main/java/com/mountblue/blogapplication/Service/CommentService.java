package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommentService {
    CommentRepository commentRepository;
    PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Set<Comment> getComments(Long postId) {
        Post post = postService.getById(postId);
        if (post != null) {
            return post.getComments();
        }
        return null;
    }

    public Comment addComment(Comment comment, Long postId) {
        Post savedPost = postService.getById(postId);
        if (savedPost != null && comment != null) {
            savedPost.addComment(comment);
            return commentRepository.save(comment);
        }
        return null;
    }

    public Comment updateComment(Comment comment, Long postId, Long commentId) {
        if (postService.isOwnerOrAdmin(postId)) {
            Comment existingComment = commentRepository.findById(commentId).orElse(null);
            if (existingComment != null) {
                Post post = existingComment.getPost();
                comment.setPost(post);
                commentRepository.save(comment);
                return comment;
            }
        }
        return null;
    }

    public boolean deleteComment(Long id, Long postId) {
        if (postService.isOwnerOrAdmin(postId)) {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null) {
                commentRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
