package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.repository.TagRepository;

public class TagService {
    private final TagRepository tagRepository;

    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
}
