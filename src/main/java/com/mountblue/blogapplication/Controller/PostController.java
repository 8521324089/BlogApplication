package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    private PostService postService;

    @Autowired
    private PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String showPosts(Model model){
        model.addAttribute("posts",postService.getAllPosts());
        return "posts";
    }

    @GetMapping("/newpost")
    public String showPostForm(Model model){
        Post post = new Post();
        model.addAttribute("post",post);
        return "new-post";
    }

    @PostMapping("/newpost")
    public String publishPost(@ModelAttribute Post post){
        postService.savePost(post);
        return "new-post";
    }
}
