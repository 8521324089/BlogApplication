package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Model.Post_tag;
import com.mountblue.blogapplication.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    private PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
}
