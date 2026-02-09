package com.jwillservices.mediastore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag extends BaseEntity {

    @Getter @Setter
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @Getter @Setter
    private Client client;

    @ManyToMany(mappedBy = "tags")
    @Getter @Setter
    private Set<Video> videos = new HashSet<>();

    public Tag() { }

    public Tag(String name, Client client) {
        this.name = name;
        this.client = client;
    }

}
