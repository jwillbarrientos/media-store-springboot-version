package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private final TagRepository tagRepository;

    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag updateTagName(Long id, String newName) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        tag.setName(newName);
        return tagRepository.save(tag);
    }
}
