package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.entity.Video.State;
import com.jwillservices.mediastore.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Video getNextVideoToDownload() {
        return videoRepository.findFirstByStateIs(State.SUBMITTED);
    }

    public Video updateVideo(String name, String path, Integer durationOfSeconds, Integer fileSize) {
        Video video = videoRepository.findFirstByStateIs(State.SUBMITTED);
        video.setName(name);
        video.setPath(path);
        video.setDurationSeconds(durationOfSeconds);
        video.setFileSize(fileSize);
        video.setState(Video.State.DOWNLOADED);
        return videoRepository.save(video);
    }

    public Video updateVideoErr(Video video) {
        video.setState(Video.State.ERROR_DOWNLOADING);
        return videoRepository.save(video);
    }

}
