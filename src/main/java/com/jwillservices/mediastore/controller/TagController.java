package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.dto.CreateTagRequest;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.repository.TagRepository;
import com.jwillservices.mediastore.service.ClientService;
import com.jwillservices.mediastore.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final TagRepository tagRepository;
    private final ClientService clientService;

    public TagController(TagService tagService, TagRepository tagRepository, ClientService clientService) {
        this.tagService = tagService;
        this.tagRepository = tagRepository;
        this.clientService = clientService;
    }

    private Client getAuthenticatedClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }
        String email = authentication.getName();
        Client client = clientService.findByEmail(email);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        return client;
    }

    @PostMapping
    public Tag addTag(@RequestBody CreateTagRequest request) {
        Client client = getAuthenticatedClient();
        return tagRepository.save(new Tag(request.getName(), client));
    }

    @GetMapping
    public List<Tag> loadAllTags() {
        Client client = getAuthenticatedClient();
        return tagRepository.findTagsByClientId(client.getId());
    }

    @PatchMapping("/{id}")
    public Tag editTag(@PathVariable Long id, @RequestParam("name") String newName) {
        return tagService.updateTagName(id, newName);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }
}
