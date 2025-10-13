package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Model.Tag;
import com.mountblue.blogapplication.Repository.PostRepository;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagService tagService;

    @Autowired
    private PostService(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public Post savePost(Post post,String tags) {
        String content = post.getContent();
        if(!content.isEmpty()) {
            String excerpt = content.trim().substring(0, Math.min(content.trim().length(), 300));
            post.setExcerpt(excerpt);
        }
        post.setTags(tagService.addTag(tags));
        if(post.getId()!=null){
            Post savedPost = postRepository.findById(post.getId()).get();
            post.setAuthor(savedPost.getAuthor());
            post.setComments(savedPost.getComments());
            post.setIs_published(savedPost.getIs_published());
            post.setPublished_at(savedPost.getPublished_at());
        }
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElse(new Post());
    }

    public Set<String> getAllAuthor() {
        return new HashSet<>(postRepository.findAllAuthors());
    }

    public void deletePost(Long id){
         postRepository.deleteById(id);
    }

    public Post getById(Long id) {
        return postRepository.findById(id).orElse(new Post());
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElse(new Post());
    }

    public List searchByKeyword(String keyword){
        return postRepository.searchByKeyword(keyword);
    }

    public List<Post> filterPosts(String keyword, List<String> authors, List<String> tags, String fromDate, String toDate, String sort) {
        return postRepository.searchFilterSort(keyword,authors,tags,fromDate,toDate,sort).stream().distinct().toList();
    }
}
