package com.jwillservices.mediastore.repository;

import com.jwillservices.mediastore.entity.Client;
import com.jwillservices.mediastore.entity.Tag;
import com.jwillservices.mediastore.entity.Video;
import com.jwillservices.mediastore.entity.Video.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findFirstByStateIs(State state);
    List<Video> findTop10ByClientOrderByCreationTimestampDesc(Client client);
    List<Video> findAllByClient(Client client);
    List<Video> findVideosByDurationSecondsIsLessThanEqual(Long durationSeconds);
    List<Video> findVideosByDurationSecondsIsGreaterThan(Long durationSeconds);
    List<Video> findByTagsIsNotEmpty();
    List<Video> findByTagsIsEmpty();
    List<Video> findByTags(Tag tag);
}
