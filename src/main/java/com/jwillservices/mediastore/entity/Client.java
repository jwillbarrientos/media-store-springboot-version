package com.jwillservices.mediastore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client extends BaseEntity {

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter @Setter
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "client")
    @Getter @Setter
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Getter @Setter
    private Set<Video> videos = new HashSet<>();

    public Client() {}

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
