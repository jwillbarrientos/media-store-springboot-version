package com.jwillservices.mediastore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Client extends BaseEntity {

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter @Setter
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter
    private List<Tag> tags = new LinkedList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter
    private List<Video> videos = new LinkedList<>();

    public Client() {}

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
