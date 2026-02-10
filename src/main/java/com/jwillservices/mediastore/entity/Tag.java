package com.jwillservices.mediastore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "client_id"})
})
public class Tag extends BaseEntity {

    @Getter @Setter
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JsonIgnore
    @Getter @Setter
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @Getter @Setter
    private Set<Video> videos = new HashSet<>();

    public Tag() { }

    public Tag(String name, Client client) {
        this.name = name;
        this.client = client;
    }

}
