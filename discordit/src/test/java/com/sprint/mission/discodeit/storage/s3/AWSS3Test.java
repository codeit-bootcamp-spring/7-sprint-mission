package com.sprint.mission.discodeit.storage.s3;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AWSS3Test {

    @Autowired
    private S3Client s3Client;

    @Test
    void s3ClientTest() {
        assertThat(s3Client).isNotNull();
    }

}