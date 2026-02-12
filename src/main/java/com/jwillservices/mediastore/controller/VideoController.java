package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.downloader.FileParser;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    public Video addVideoByLink(@RequestParam String link, HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }

        return videoService.createVideoSubmittedByLink(link, client);
    }

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public List<Video> addVideoByFile(@RequestParam("chatFile") MultipartFile file, HttpSession session) throws IOException {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }

        return videoService.createVideosSubmittedByFile(new String(file.getBytes(), StandardCharsets.UTF_8), client);
    }

    @GetMapping
    public List<Video> loadVideos(HttpSession session) {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }

        return videoService.getTheLast10Videos(client);
    }

    @GetMapping("/{id}/tags")
    public List<Tag> getTags(@PathVariable Long id) {
        return videoService.getTagsByVideoId(id);
    }

    //@GetMapping("/reel")
    //public List<Video> getVideosForReel() {
//
    //}
//

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
        videoService.deleteVideoById(id);
    }
}
