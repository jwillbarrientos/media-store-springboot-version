package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.downloader.FileParser;
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
        List<Video> videos = new ArrayList<>(); // vos me escuchas?sisi
        for (String link : links) {
            videos.add(createVideoSubmittedByLink(link, client));
        }
        return videos;
    }

    // todo borrar
    public Video getVideoById(Long id) {
        return videoRepository.getById(id);
    }

    // todo borrar
    public List<Tag> getTagsByVideoId(Long id) {
        return videoRepository.getById(id).getTags();
    }

    // todo borrar
    public List<Video> getTheLast10Videos(Client client) {
        return videoRepository.findTop10ByClientOrderByCreationTimestampDesc(client);
    }

    public enum VideoTag { // todo refactor a otra parte
        ALL("all"),
        LESS_THAN_A_MINUTE("lte60"),
        MORE_THAN_A_MINUTE("bt60"),
        WITH_TAGS("with"),
        WITHOUT_TAGS("without"),
        CUSTOM("custom");

        public final String tagName;
        VideoTag(String tagName) {
            this.tagName = tagName;
        }

        public static VideoTag fromName(String tagName) {
            for (VideoTag defaultTag : VideoTag.values()) {
                if (defaultTag.tagName.equalsIgnoreCase(tagName)) {
                    return defaultTag;
                }
            }
            return CUSTOM;
        }
    }

    public List<Video> getVideosByTag(Client client, VideoTag tag, String customTag) {
        switch (tag) {
            case ALL:                return videoRepository.findAllByClient(client);
            case LESS_THAN_A_MINUTE: return videoRepository.findVideosByDurationSecondsIsLessThanEqual(60L);
            case MORE_THAN_A_MINUTE: return videoRepository.findVideosByDurationSecondsIsGreaterThan(60L);
            case WITH_TAGS:          return videoRepository.findByTagsIsNotEmpty();
            case WITHOUT_TAGS:       return videoRepository.findByTagsIsEmpty();
            default:                 return videoRepository.findByTags(tagRepository.getById(Long.parseLong(customTag)));
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

    // todo borrar
    public void deleteVideoById(Long id) {
        videoRepository.deleteById(id);
    }

}
