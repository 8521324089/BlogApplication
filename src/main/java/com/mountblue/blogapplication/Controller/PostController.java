package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Service.PostService;
import com.mountblue.blogapplication.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @Autowired
    private PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String showPosts(Model model){
        model.addAttribute("posts",postService.getAllPosts());
        model.addAttribute("authors",postService.getAllAuthor());
        model.addAttribute("tags",tagService.getAllTags());
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
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);
        model.addAttribute("comment",new Comment());
        return "post-detail";
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(@PathVariable("id") Long id){
         postService.deletePost(id);
         return "redirect:/";
    }

    @GetMapping("/post/{id}/update")
    public String updatePost(@PathVariable("id") Long id,Model model){
        Post post = postService.getById(id);
        model.addAttribute("post",post);
        model.addAttribute("allTag", post.getAllTags());
        return "new-post";
    }

    @GetMapping("/post/search")
    public String searchPost(@RequestParam String keyword,Model model){
        model.addAttribute("posts",postService.searchByKeyword(keyword));
        model.addAttribute("authors",postService.getAllAuthor());
        return "posts";
    }

    @GetMapping("/post/filter")
    public String filterPost(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) List<String> authors,
                             @RequestParam(required = false) List<String> tags,
                             @RequestParam(required = false) String fromDate,
                             @RequestParam(required = false) String toDate,
                             @RequestParam(required = false) String sort,
                             Model model){
        List<Post> filteredPosts = postService.filterPosts(keyword,authors, tags, fromDate, toDate,sort);
        model.addAttribute("keyword",keyword);
        model.addAttribute("selectedAuthors", authors);
        model.addAttribute("selectedTags",tags);
        model.addAttribute("fromDate",fromDate);
        model.addAttribute("toDate",toDate);
        model.addAttribute("selectedSort",sort);
        model.addAttribute("posts", filteredPosts);
        model.addAttribute("authors",postService.getAllAuthor() );
        model.addAttribute("tags", tagService.getAllTags());
        return "posts";
    }
}
