package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.downloader.FileParser;
import com.jwillservices.mediastore.downloader.VideoTag;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.entity.Video.State;
import com.jwillservices.mediastore.repository.TagRepository;
import com.jwillservices.mediastore.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final TagRepository tagRepository;
    private final FileParser fileParser;

    public VideoService(VideoRepository videoRepository, TagRepository tagRepository, FileParser fileParser) {
        this.videoRepository = videoRepository;
        this.tagRepository = tagRepository;
        this.fileParser = fileParser;
    }

    public Video createVideoSubmittedByLink(String link, Client client) {
        return videoRepository.save(new Video(link, State.SUBMITTED, client));
    }

    public List<Video> createVideosSubmittedByFile(String file, Client client) {
        String body = fileParser.parseBody(file);
        String[] links = body.split("\n");
        List<Video> videos = new ArrayList<>();
        for (String link : links) {
            videos.add(createVideoSubmittedByLink(link, client));
        }
        return videos;
    }

    public List<Video> getVideosByTag(Client client, VideoTag tag, String customTag) {
        switch (tag) {
            case ALL:                return videoRepository.findAllByClientAndState(client, State.DOWNLOADED);
            case LESS_THAN_A_MINUTE: return videoRepository.findVideosByDurationSecondsIsLessThanEqualAndState(60L, State.DOWNLOADED);
            case MORE_THAN_A_MINUTE: return videoRepository.findVideosByDurationSecondsIsGreaterThanAndState(60L, State.DOWNLOADED);
            case WITH_TAGS:          return videoRepository.findByTagsIsNotEmptyAndState(State.DOWNLOADED);
            case WITHOUT_TAGS:       return videoRepository.findByTagsIsEmptyAndState(State.DOWNLOADED);
            default:                 return videoRepository.findByTagsAndState(tagRepository.getById(Long.parseLong(customTag)), State.DOWNLOADED);
        }
    }

    public Video addTagToVideo(Long tagId, Long videoId) {
        Tag tag = tagRepository.getById(tagId);
        Video video = videoRepository.getById(videoId);
        video.getTags().add(tag);
        return videoRepository.save(video);
    }

    public Video deleteTagFromVideo(Long tagId, Long videoId) {
        Tag tag = tagRepository.getById(tagId);
        Video video = videoRepository.getById(videoId);
        video.getTags().remove(tag);
        return videoRepository.save(video);
    }
}
