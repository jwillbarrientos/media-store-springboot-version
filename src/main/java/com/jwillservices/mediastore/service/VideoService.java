package com.jwillservices.mediastore.service;

import com.jwillservices.mediastore.downloader.FileParser;
import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.entity.Video.State;
import com.jwillservices.mediastore.repository.TagRepository;
import com.jwillservices.mediastore.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final TagRepository tagRepository;
    private final FileParser fileParser;
    private final List<Video> videos;

    public VideoService(VideoRepository videoRepository, TagRepository tagRepository, FileParser fileParser, List<Video> videos) {
        this.videoRepository = videoRepository;
        this.tagRepository = tagRepository;
        this.fileParser = fileParser;
        this.videos = videos;
    }

    public Video createVideoSubmittedByLink(String link, Client client) {
        Video video = new Video(link, State.SUBMITTED, client);
        return videoRepository.save(video);
    }

    public List<Video> createVideosSubmittedByFile(String file, Client client) {
        String body = fileParser.parseBody(file);
        String[] links = body.split("\n");
        for (String link : links) {
            videos.add(this.createVideoSubmittedByLink(link, client));
        }
        return videos;
    }

    public Video getVideoById(Long id) {
        return videoRepository.getById(id);
    }

    public List<Tag> getTagsByVideoId(Long id) {
        return videoRepository.getById(id).getTags();
    }

    public Video getNextVideoToDownload() {
        return videoRepository.findFirstByStateIs(State.SUBMITTED);
    }

    public List<Video> getTheLast10Videos(Client client) {
        return videoRepository.findTop10ByClientOrderByCreationTimestampDesc(client);
    }

    public List<Video> getVideosByTag(Client client, String tag) {
        if (tag.equals("all")) {
            return videoRepository.findAllByClient(client);
        } else if (tag.equals("lte60")) {
            return videoRepository.findVideosByDurationSecondsIsLessThanEqual(60L);
        } else if (tag.equals("bt60")) {
            return videoRepository.findVideosByDurationSecondsIsGreaterThan(60L);
        } else if (tag.equals("with")) {
            return videoRepository.findByTagsIsNotEmpty();
        } else if (tag.equals("without")) {
            return videoRepository.findByTagsIsEmpty();
        }
        return videoRepository.findByTags(tagRepository.getById(Long.parseLong(tag)));
    }

    public Video updateVideo(String name, String path, Long durationOfSeconds, Long fileSize) {
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

    public Video addTagToVideo(Long tagId, Long videoId) {
        Tag tag = tagRepository.getById(tagId);
        Video video = videoRepository.getById(videoId);
        List<Tag> tags = video.getTags();
        tags.add(tag);
        video.setTags(tags);
        return videoRepository.save(video);
    }

    public Video deleteTagFromVideo(Long tagId, Long videoId) {
        Tag tag = tagRepository.getById(tagId);
        Video video = videoRepository.getById(videoId);
        List<Tag> tags = video.getTags();
        tags.remove(tag);
        video.setTags(tags);
        return videoRepository.save(video);
    }

    public void deleteVideoById(Long id) {
        videoRepository.deleteById(id);
    }

}
