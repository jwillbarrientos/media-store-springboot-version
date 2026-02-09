package com.jwillservices.mediastore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@MappedSuperclass
public class BaseEntity {

    @Id
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private Instant creationTimestamp;

    private static AtomicLong idSeq = new AtomicLong(System.currentTimeMillis());
    @PrePersist
    protected void onCreate() {
        id = idSeq.getAndIncrement();
        this.creationTimestamp = Instant.now();
    }
}
