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
    List<Video> findTop10ByClientAndStateOrderByCreationTimestampDesc(Client client, State state);
    List<Video> findAllByClientAndState(Client client, State state);
    List<Video> findVideosByDurationSecondsIsLessThanEqualAndState(Long durationSeconds, State state);
    List<Video> findVideosByDurationSecondsIsGreaterThanAndState(Long durationSeconds, State state);
    List<Video> findByTagsIsNotEmptyAndState(State state);
    List<Video> findByTagsIsEmptyAndState(State state);
    List<Video> findByTagsAndState(Tag tags, State state);
}
