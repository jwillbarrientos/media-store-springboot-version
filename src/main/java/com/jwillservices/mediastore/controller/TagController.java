package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.dto.CreateTagRequest;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public Tag addTag(@RequestBody CreateTagRequest request, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }
        return tagService.createTag(request.getName(), client);
    }

    @GetMapping
    public List<Tag> loadAllTags(HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }
        return tagService.getTagsByClientId(client.getId());
    }

    //@PatchMapping("/{id}")
    //public Tag editTag(@PathVariable Long id, @RequestBody Tag request) {
    //    //nothing yet
    //}

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTagById(id);
    }
}
