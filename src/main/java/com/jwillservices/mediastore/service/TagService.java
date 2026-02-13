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

    // todo borrar
    public Tag createTag(String name, Client client) {
        return tagRepository.save(new Tag(name, client));
    }

    // todo borrar
    public List<Tag> getTagsByClientId(Long clientId) {
        return tagRepository.findTagsByClientId(clientId);
    }

    public Tag updateTagName(Long id, String newName) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        tag.setName(newName);
        return tagRepository.save(tag);
    }

    // todo borrar
    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }
}
