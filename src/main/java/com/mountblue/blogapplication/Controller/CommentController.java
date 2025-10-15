package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Repository.CommentRepository;
import com.mountblue.blogapplication.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post/{postId}")
public class CommentController {
    CommentService commentService;
    CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public String addComment(@PathVariable Long postId, @ModelAttribute Comment comment) {
        commentService.addComment(postId, comment);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/comment/update/{id}")
    public String showCommentForm(@PathVariable Long postId, @PathVariable Long id, Model model) {
        Comment comment = commentService.findById(id,postId);
        if(comment!=null) {
            model.addAttribute("postId", postId);
            model.addAttribute("comment", comment);
            return "edit_comment";
        }
        else{
            return "redirect:/post/{postId}";
        }
    }

    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long id, @ModelAttribute Comment comment) {
        commentService.updateComment(comment,postId);
            return "redirect:/post/" + postId;
    }

    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long id) {
        commentService.deleteComment(id,postId);
            return "redirect:/post/" + postId;
    }
}
