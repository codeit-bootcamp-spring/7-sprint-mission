package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;

    private String filename;
    private String contentType;
    private byte[] data;

    public BinaryContent(String filename, String contentType, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.filename = filename;
        this.contentType = contentType;
        this.data = data;
    }
}
