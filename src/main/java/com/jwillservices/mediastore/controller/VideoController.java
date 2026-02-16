package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.downloader.VideoTag;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.repository.VideoRepository;
import com.jwillservices.mediastore.service.ClientService;
import com.jwillservices.mediastore.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;
    private final VideoRepository videoRepository;
    private final ClientService clientService;

    public VideoController(VideoService videoService, VideoRepository videoRepository, ClientService clientService) {
        this.videoService = videoService;
        this.videoRepository = videoRepository;
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
    public Video addVideoByLink(@RequestParam String link) {
        Client client = getAuthenticatedClient();
        return videoService.createVideoSubmittedByLink(link, client);
    }

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public List<Video> addVideoByFile(@RequestParam("chatFile") MultipartFile file) throws IOException {
        Client client = getAuthenticatedClient();
        return videoService.createVideosSubmittedByFile(new String(file.getBytes(), StandardCharsets.UTF_8), client);
    }

    @GetMapping
    public List<Video> loadVideos() {
        Client client = getAuthenticatedClient();
        return videoRepository.findTop10ByClientAndStateOrderByCreationTimestampDesc(client, Video.State.DOWNLOADED);
    }

    @GetMapping("/{id}/tags")
    public List<Tag> getTags(@PathVariable Long id) {
        return videoRepository.getById(id).getTags();
    }

    @GetMapping("/reel")
    public List<Video> getVideosForReel(@RequestParam String tag) {
        Client client = getAuthenticatedClient();
        return videoService.getVideosByTag(client, VideoTag.fromName(tag), tag);
    }

    @PatchMapping("/add/tag/{tagId}/video/{videoId}")
    public Video addTagToVideo(@PathVariable Long tagId, @PathVariable Long videoId) {
        return videoService.addTagToVideo(tagId, videoId);

    }

    @PatchMapping("/delete/tag/{tagId}/video/{videoId}")
    public Video deleteTagFromVideo(@PathVariable Long tagId, @PathVariable Long videoId) {
        return videoService.deleteTagFromVideo(tagId, videoId);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoRepository.deleteById(id);
    }
}
