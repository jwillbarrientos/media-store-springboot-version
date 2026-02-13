package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.downloader.LimitedInputStream;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.service.VideoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/api/videoactions")
public class VideoActionsController {
    private final VideoService videoService;

    public VideoActionsController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadVideoById(@PathVariable Long id, HttpSession session) throws IOException {
        Client client = (Client) session.getAttribute("client");
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user logged in");
        }

        Video video = videoService.getVideoById(id);
        Path path = Paths.get(video.getPath());

        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        Resource resource = new UrlResource(path.toUri());

        String filename = path.getFileName().toString();
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(Files.size(path))
                .body(resource);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long id, @RequestHeader HttpHeaders headers) throws IOException {
        Video video = videoService.getVideoById(id);
        Path path = Paths.get(video.getPath());
        long fileSize = video.getFileSize();

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        long rangeStart = 0;
        long rangeEnd = fileSize - 1;

        List<HttpRange> ranges = headers.getRange();
        if (!ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            rangeStart = range.getRangeStart(fileSize);
            rangeEnd = range.getRangeEnd(fileSize);
        }

        long contentLength = rangeEnd - rangeStart + 1;

        InputStream inputStream = Files.newInputStream(path);
        inputStream.skip(rangeStart);

        InputStreamResource resource = new InputStreamResource(new LimitedInputStream(inputStream, contentLength));

        return ResponseEntity.status(ranges.isEmpty() ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                .contentLength(contentLength)
                .body(resource);
    }
}
