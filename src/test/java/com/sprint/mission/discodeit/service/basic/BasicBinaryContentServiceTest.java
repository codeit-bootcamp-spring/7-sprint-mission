package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContentUsage;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BasicBinaryContentServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BasicBinaryContentServiceTest.class);
    @Autowired
    private BasicBinaryContentService basicBinaryContentService;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        binaryContentRepository.resetBinaryContentRepository();
    }

    @Test
    @DisplayName("[정상 케이스] - 바이너리 컨텐츠 생성")
    void createBinaryContent() {
        //given
        var binaryContent = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t1.png".getBytes(), BinaryContentUsage.PROFILE));
        //when
        var actualResult = binaryContentRepository.readAllBinaryContent().stream().anyMatch(x->x.getId().equals(binaryContent.getId()));
        //then
        assertThat(actualResult).isTrue();
    }

    @Test
    @DisplayName("[정상 케이스] - 바이너리 컨텐츠 조회")
    void find() {
        //given
        var binaryContent = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t1.png".getBytes(), BinaryContentUsage.PROFILE));
        //when
        var actualResult = basicBinaryContentService.find(binaryContent.getId());

        //then
        assertThat(actualResult.getBinaryFile()).isEqualTo(binaryContent.getBinaryFile());
    }

    @Test
    @DisplayName("[정상 케이스] - 바이너리 컨텐츠 id로 전체 조회")
    void findAllByIdIn() {
        //given
        var binaryContent1 = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t1.png".getBytes(), BinaryContentUsage.PROFILE));
        var binaryContent2 = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t2.png".getBytes(), BinaryContentUsage.PROFILE));
        //when
        var actualResult = basicBinaryContentService.findAllByIdIn(List.of(binaryContent1.getId(), binaryContent2.getId()));
        //then
        assertThat(actualResult.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("[예외 케이스] - 바이너리 컨텐츠 포함 안된 id 로 조회")
    void findNonExistId(){
        //given
        var binaryContent1 = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t1.png".getBytes(), BinaryContentUsage.PROFILE));
        var binaryContent2 = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t2.png".getBytes(), BinaryContentUsage.PROFILE));
        //when
        assertThrows(IllegalArgumentException.class, ()->basicBinaryContentService.find(UUID.randomUUID()));
    }

    @Test
    @DisplayName("[정상 케이스] - 바이너리 컨텐츠 삭제")
    void deleteBinaryContent() {
        //given
        var binaryContent1 = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t1.png".getBytes(), BinaryContentUsage.PROFILE));
        var binaryContent2 = basicBinaryContentService.createBinaryContent
                (new BinaryContentCreateRequestDto("t2.png".getBytes(), BinaryContentUsage.PROFILE));
        //when
        basicBinaryContentService.deleteBinaryContent(binaryContent1.getId());
        var actualResult = binaryContentRepository.readAllBinaryContent().stream().noneMatch(x->x.getId().equals(binaryContent1.getId()));
        //then
        assertThat(actualResult).isTrue();
    }
}