package com.jwillservices.mediastore.controller;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.repository.VideoRepository;
import com.jwillservices.mediastore.service.ClientService;
import com.jwillservices.mediastore.service.VideoService;
import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/api/videoactions")
public class VideoActionsController {
    public static final long CHUNK_SIZE_BYTES = 1024 * 256L;
    private final VideoRepository videoRepository;
    private final ClientService clientService;

    public VideoActionsController(VideoRepository videoRepository, ClientService clientService) {
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

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadVideoById(@PathVariable Long id) throws IOException {
        Client client = getAuthenticatedClient();

        Video video = videoRepository.getById(id);
        Path path = Paths.get(video.getPath());

        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        String filename = path.getFileName().toString();

        return ResponseEntity.ok()
                .contentType(getMediaType(path))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(video.getFileSize())
                .body(new UrlResource(path.toUri()));
    }

    private static @NonNull MediaType getMediaType(Path path) throws IOException {
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return MediaType.parseMediaType(contentType);
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long id, @RequestHeader HttpHeaders headers) throws IOException {
        Video video = videoRepository.getById(id);
        if (video.getState() != Video.State.DOWNLOADED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Video not available for streaming");

        Path path = Paths.get(video.getPath());
        long fileSize = video.getFileSize();

        long rangeStart = 0;
        long rangeEnd = fileSize - 1;

        List<HttpRange> ranges = headers.getRange();
        if (!ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            rangeStart = range.getRangeStart(fileSize);
            rangeEnd = range.getRangeEnd(fileSize);
        }

        long contentLength = rangeEnd - rangeStart + 1;

        RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");
        file.seek(rangeStart);
        byte[] chunk = new byte[(int) contentLength];
        file.readFully(chunk);
        Resource resource = new ByteArrayResource(chunk);
        rangeEnd = Math.min(rangeStart + CHUNK_SIZE_BYTES - 1, path.toFile().length() - 1);
        return ResponseEntity.status(ranges.isEmpty() ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT)
                .contentType(getMediaType(path))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %s-%s/%s", rangeStart, rangeEnd, fileSize))
                .contentLength(contentLength)
                .body(resource);
    }
}
