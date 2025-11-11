package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    //Field
    private UUID id;                  //각 객체 UUID
    private Instant createdAt;        //객체 생성 일시
    private byte[] data;              //이미지의 데이터
    private String fileName;          //파일이름
    private String fileType;          //파일 형식

    //Constructor
    private BinaryContent(byte[] data, String fileName, String fileType){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = data;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    //Factory Method
    public static BinaryContent create(byte[] data, String fileName, String fileType){
        return new BinaryContent(data, fileName, fileType);
    }
}
