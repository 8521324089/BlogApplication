package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Model.User;
import com.mountblue.blogapplication.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    private PostService(PostRepository postRepository, TagService tagService, UserService userService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.userService = userService;
    }

    public boolean getAuthentication(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        Post post = getById(id);
        if (post == null) {
            return false;
        }

        User currentUser = (User) userService.getByUsername(authentication.getName()).orElse(new User());
        return currentUser.getRole().equals("ADMIN") || (post.getUser() != null && post.getUser().getEmail().equals(currentUser.getEmail()));
    }

    public Post savePost(Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.getByUsername(authentication.getName()).orElse(null);
        if(user!=null) {
            post.setUser(user);
            post.setAuthor(user.getName());
        }
        return postRepository.save(post);
    }

    public Post savePost(Post post, String tags){
        String content = post.getContent();
        if (content != null && !content.isEmpty()) {
            String excerpt = content.trim().substring(0, Math.min(content.trim().length(), 300));
            post.setExcerpt(excerpt);
        }
        post.setTags(tagService.addTag(tags));
        if (post.getId() != null) {
            Post existingPost = postRepository.findById(post.getId()).orElse(null);
            if(existingPost!=null && getAuthentication(existingPost.getId())){
                post.setAuthor(existingPost.getAuthor());
                post.setComments(existingPost.getComments());
                post.setIs_published(existingPost.getIs_published());
                post.setPublishedAt(existingPost.getPublishedAt());
                post.setUser(existingPost.getUser());
            }else new Post();
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) userService.getByUsername(authentication.getName()).orElse(new User());
            post.setUser(user);
            post.setAuthor(user.getName());
        }

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(int page, int size) {
        if (size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable);
    }

    public Post findPostById(Long id) {
        if(getAuthentication(id)){
            return postRepository.findById(id).orElse(new Post());
        }
        return null;
    }

    public Set<String> getAllAuthor() {
        return new HashSet<>(postRepository.findAllAuthors());
    }

    public boolean deletePost(Long id) {
        if (getAuthentication(id)){
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Post getById(Long id) {
        return postRepository.findById(id).orElse(new Post());
    }

    public Page<Post> searchByKeyword(String keyword, int page, int size) {
        if (size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.searchByKeyword(pageable, keyword);
    }

    public Page<Post> filterPosts(String keyword, List<String> authors, List<String> tags, LocalDate fromDate, LocalDate toDate, String sort, int page, int size) {
        if (size < 1) size = 10;
        if (page < 0) page = 0;

        Pageable pageable;
        if (sort != null && sort.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        }

        boolean hasAuthors = authors != null && !authors.isEmpty();
        boolean hasTags = tags != null && !tags.isEmpty();
        Long tagsSize = tags==null?(long)0:(long)tags.size();
        Long authorSize = authors==null?(long)0:(long)authors.size();

        if(authors!=null)
            authors = authors.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        if(tags!=null)
            tags = tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        if(toDate!=null)
            toDate = toDate.plusDays(1);
        return postRepository.searchFilterSort(keyword, authors, tags, fromDate, toDate, hasAuthors, hasTags, tagsSize, authorSize, pageable);
    }
}
