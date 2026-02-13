package com.jwillservices.mediastore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Video extends BaseEntity {

    public enum State {
        SUBMITTED, DOWNLOADED, ERROR_DOWNLOADING, ACTIVE
    }

    @ManyToOne(optional = false)
    @JsonIgnore
    @Getter @Setter
    private Client client;

    @Getter @Setter
    @Column(nullable = false)
    private String link;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String path;

    @Getter @Setter
    private Long durationSeconds;

    @Getter @Setter
    private Long fileSize;

    @ManyToMany
    @JoinTable(
            name = "VIDEO_TAGS",
            joinColumns = @JoinColumn(name = "VIDEOS_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAGS_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"VIDEOS_ID", "TAGS_ID"})

    )
    @JsonIgnore
    @Getter @Setter
    private List<Tag> tags = new LinkedList<>();

    public Video() {
    }

    public Video(String link, State state, Client client) {
        this("", link, "", 0L, 0L, state, client);
    }

    public Video(String name,
                 String link,
                 String path,
                 Long durationSeconds,
                 Long fileSize,
                 State state,
                 Client client){
        this.name = name;
        this.link = link;
        this.path = path;
        this.durationSeconds = durationSeconds;
        this.fileSize = fileSize;
        this.state = state;
        this.client = client;
    }

}
