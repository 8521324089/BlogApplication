package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostController {
    private PostService postService;

    @Autowired
    private PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String showPosts(Model model){
        List<Post> li = postService.getAllPosts();
        if(!li.isEmpty())
        System.out.println(li.get(0).getTags());
        model.addAttribute("posts",li);
        return "posts";
    }

    @GetMapping("/newpost")
    public String showPostForm(Model model){
        Post post = new Post();
        model.addAttribute("post",post);
        return "new-post";
    }

    @PostMapping("/newpost")
    public String publishPost(@ModelAttribute Post post, @RequestParam String allTag){
        postService.savePost(post,allTag);
        return "posts";
    }
}
