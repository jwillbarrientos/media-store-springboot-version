package com.jwillservices.mediastore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Video extends BaseEntity {

    public enum State {
        SUBMITTED, DOWNLOADED, ERROR_DOWNLOADING, ACTIVE
    }

    @ManyToOne(optional = false)
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
    private Integer durationSeconds;

    @Getter @Setter
    private Integer fileSize;

    @ManyToMany
    @JoinTable(
            name = "VIDEO_TAGS",
            joinColumns = @JoinColumn(name = "VIDEOS_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAGS_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"VIDEOS_ID", "TAGS_ID"})

    )
    @Getter @Setter
    private Set<Tag> tags = new HashSet<>();

    public Video() {
    }

    public Video(String link, State state, Client client) {
        this("", link, "", 0, 0, state, client);
    }

    public Video(String name,
                 String link,
                 String path,
                 Integer durationSeconds,
                 Integer fileSize,
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
