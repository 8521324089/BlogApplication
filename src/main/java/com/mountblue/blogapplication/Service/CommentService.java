package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    CommentRepository commentRepository;
    PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public void addComment(Long postId,Comment comment) {
        Post savedPost = postService.getById(postId);
        savedPost.addComment(comment);

        postService.savePost(savedPost);
    }

    public void updateComment(Comment comment) {
        Comment existingComment = commentRepository.findById(comment.getId()).orElse(new Comment());
        Post post = existingComment.getPost();
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).get();
    }

}
