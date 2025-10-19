package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.Comment;
import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Model.User;
import com.mountblue.blogapplication.Service.PostService;
import com.mountblue.blogapplication.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public String showPosts(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<Post> reqPosts = postService.getAllPosts(page, size);
        model.addAttribute("posts", reqPosts.getContent());
        model.addAttribute("authors", postService.getAllAuthor());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("currPage", page);
        model.addAttribute("totalPages", reqPosts.getTotalPages());
        model.addAttribute("totalItems", reqPosts.getTotalElements());
        return "posts";
    }

    @GetMapping("/newpost")
    public String showPostForm(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "new-post";
    }

    @PostMapping("/newpost")
    public String publishPost(@ModelAttribute Post post, @RequestParam String allTag) {
        Post savedPost = postService.savePost(post, allTag);
        return "redirect:/post/" + savedPost.getId();
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model) {
        Post post = postService.getById(id);
        User user = post.getUser();
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());
        if (user != null) {
            model.addAttribute("uName", user.getEmail());
        }
        return "post-detail";
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        if (postService.deletePost(id))
            return "redirect:/";
        else return "redirect:/post/" + id;
    }

    @GetMapping("/post/{id}/update")
    public String updatePost(@PathVariable("id") Long id, Model model) {
        Post post = postService.findPostById(id);
        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("allTag", post.getAllTags());
            return "new-post";
        }
        return "redirect:/post/{id}";
    }

    @GetMapping("/post/filter")
    public String filterPost(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String prevKeyword,
                             @RequestParam(required = false) List<String> authors,
                             @RequestParam(required = false) List<String> tags,
                             @RequestParam(required = false) LocalDate fromDate,
                             @RequestParam(required = false) LocalDate toDate,
                             @RequestParam(required = false) String sort,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<Post> reqPosts;
        if (prevKeyword != null && (!prevKeyword.
                isEmpty()) && (!prevKeyword.equals(keyword))) {
            reqPosts = postService.searchByKeyword(keyword, page, size);
        } else {
            reqPosts = postService.filterPosts(keyword, authors, tags, fromDate, toDate, sort, page, size);
            model.addAttribute("selectedAuthors", authors);
            model.addAttribute("selectedTags", tags);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("selectedSort", sort);
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", reqPosts.getContent());
        model.addAttribute("authors", postService.getAllAuthor());
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("prevKeyword", keyword);
        model.addAttribute("currPage", page);
        model.addAttribute("totalPages", reqPosts.getTotalPages());
        model.addAttribute("totalItems", reqPosts.getTotalElements());
        return "posts";
    }
}
