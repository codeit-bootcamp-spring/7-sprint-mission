package com.sprint.mission.discodeit.file.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.binaryContent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("BinaryContentService Unit Test")
public class BinaryContentServiceUnitTest {

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private BasicBinaryContentService binaryContentService;

    private BinaryContent binaryContent;
private UUID binaryContentId;
    @BeforeEach
    void setUp() {
        binaryContent = new BinaryContent(

                "fileName",
                "text/plain",
                10L
        );
       binaryContentId = UUID.randomUUID();
        ReflectionTestUtils.setField(binaryContent,"id",binaryContentId);
    }
    @Test
    @DisplayName("[정상 케이스] 파일 다운로드 성공")
    void downloadFile_Success() {

        given(binaryContentRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(binaryContent));

        given(binaryContentStorage.download(any(BinaryContentDto.class)))
                .willReturn(
                        new ResponseEntity<>(HttpStatus.OK)
                );

        ResponseEntity<?> response = binaryContentService.downloadFile(binaryContentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(binaryContentStorage).should(times(1)).download(any(BinaryContentDto.class));

    }

    @Test
    @DisplayName("[정상 케이스] 파일 id로 조회 성공")
    void findById_Success() {

        given(binaryContentRepository.findById(any(UUID.class)))
                .willReturn(java.util.Optional.of(binaryContent));

        BinaryContentDto response = binaryContentService.find(binaryContentId);

        assertThat(response.fileName()).isEqualTo(binaryContent.getFileName());
        assertThat(response.size()).isEqualTo(binaryContent.getSize());

        then(binaryContentRepository).should(times(1)).findById(any(UUID.class));


    }

    @Test
    @DisplayName("[정상 케이스] 파일 생성 성공")
    void createFile_Success() {

        given(binaryContentRepository.save(any(BinaryContent.class)))
                .willReturn(binaryContent);
        given(binaryContentStorage.put(any(),any(byte[].class)))
                .willReturn(UUID.randomUUID());

        var response = binaryContentService.createBinaryContent(
                new BinaryContentCreateRequestDto(
                        "111".getBytes(),
                        "Hello",
                        5L,
                        "text/plain"
                )
        );

        assertThat(response.fileName()).isEqualTo(binaryContent.getFileName());
        assertThat(response.size()).isEqualTo(binaryContent.getSize());

        then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
        then(binaryContentStorage).should(times(1)).put(any(),any(byte[].class));

    }

}
