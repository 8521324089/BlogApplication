package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Service.CommentService;
import com.mountblue.blogapplication.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post/{postId}")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/comment")
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment) {
        commentService.addComment(comment, postId);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/comment/update/{id}")
    public String showCommentForm(@PathVariable Long postId, @PathVariable Long id, Model model) {
        Comment comment = commentService.findById(id);
        if (postService.isOwnerOrAdmin(postId)) {
            model.addAttribute("postId", postId);
            model.addAttribute("comment", comment);
            return "edit_comment";
        } else {
            return "redirect:/post/{postId}";
        }
    }

    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long id, @ModelAttribute Comment comment) {
        commentService.updateComment(comment, postId, id);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long id) {
        commentService.deleteComment(id, postId);
        return "redirect:/post/" + postId;
    }
}
