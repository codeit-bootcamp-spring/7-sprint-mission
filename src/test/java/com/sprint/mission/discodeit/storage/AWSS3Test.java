package com.sprint.mission.discodeit.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:.env")
public class AWSS3Test {

    @Autowired
    S3Properties s3Properties;

    @Autowired
    S3BinaryContentStorage s3BinaryContentStorage;

    @Value("${discodeit.storage.s3.access-key}")
    String accessKey;

    @Test
    void printRProperties(){
        System.out.println(s3Properties.getAccessKey());
        System.out.println(s3Properties.getSecretKey());
        System.out.println(s3Properties.getRegion());
        System.out.println(s3Properties.getBucket());

//        System.out.println(s3BinaryContentStorage.getAccessKey());

        System.out.println(accessKey);
    }

    @Test
    void upload(){

    }

    @Test
    void download(){

    }

    @Test
    void presignedUrl(){

    }
}
