package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag createTag(String name, Client client) {
        Tag tag = new Tag(name, client);
        return tagRepository.save(tag);
    }

    public List<Tag> getTagsByClientId(Long clientId) {
        return tagRepository.findTagsByClientId(clientId);
    }

    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }
}
