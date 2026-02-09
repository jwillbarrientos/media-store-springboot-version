package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.repository.VideoRepository;

public class VideoService {
    private final VideoRepository videoRepository;

    VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }
}
