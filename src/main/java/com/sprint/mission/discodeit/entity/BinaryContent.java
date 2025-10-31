package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID id;                  //각 객체 UUID
    private final Instant createdAt;        //객체 생성 일시
    private final byte[] data;              //이미지의 데이터
    private final String fileName;          //파일이름
    private final String fileType;          //파일 형식

    //Constructor
    public BinaryContent(byte[] data, String fileName, String fileType){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = data;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
