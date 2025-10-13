package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Post;
import com.mountblue.blogapplication.Model.Tag;
import com.mountblue.blogapplication.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> addTag(String tags){
        Set<Tag> allTags = new HashSet<>();
        if(!tags.trim().isEmpty()){
            Set<String> tagSet = new HashSet<>(List.of(tags.trim().split(",")));
            for(String tagName:tagSet){
                if(!tagName.trim().isEmpty()){
                    Tag savedTag = tagRepository.findByName(tagName.trim());
                    if(savedTag==null){
                        savedTag = tagRepository.save(new Tag(tagName.trim(),new HashSet<Post>()));
                    }
                    allTags.add(savedTag);
                }
            }
        }
        return allTags;
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }
}
