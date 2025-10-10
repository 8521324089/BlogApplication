package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Model.Tag;
import com.mountblue.blogapplication.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    private PostRepository postRepository;
    private TagService tagService;

    @Autowired
    private PostService(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    public Post savePost(Post post,String tags) {
        post.setTags(new HashSet<Tag>());
        if(!tags.trim().isEmpty()){
            Set<String> tagSet = new HashSet<>(List.of(tags.trim().split(",")));
            for(String tagName:tagSet){
                if(!tagName.trim().isEmpty()){
                    Tag savedTag = tagService.findByName(tagName);
                    if(savedTag==null){
                        savedTag = tagService.saveTag(new Tag(tagName,new HashSet<Post>()));
                    }
                    post.getTags().add(savedTag);
                }
            }
        }
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        List<Post> ans = postRepository.findAll();
        System.out.println(ans);
        return ans;
    }
}
