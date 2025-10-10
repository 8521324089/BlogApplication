package com.mountblue.blogapplication.Service;

import com.mountblue.blogapplication.Model.Tag;
import com.mountblue.blogapplication.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Tag saveTag(Tag tag){
        return tagRepository.save(tag);
    }

    public Tag findByName(String name){
        return tagRepository.findByName(name);
    }
}
