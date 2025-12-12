package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class BinaryContentServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    BinaryContentService binaryContentService;

    @Autowired
    BinaryContentRepository binaryContentRepository;

    @Autowired
    BinaryContentStorage binaryContentStorage;


    @Test
    @DisplayName("[정상 케이스] 파일 조회")
    void find() throws IOException {
        //given
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "dummy.txt",
                "application/octet-stream",  // 일반적으로 그냥 placeholder로 사용 가능
                new byte[0]
        );
        var userDto = userService.createUser(TestFixture.userCreateFactory(),multipartFile);

        //when
        BinaryContentDto binaryContentDto = binaryContentService.find(userDto.profile().id());

        //then
        var actualResult = binaryContentDto.fileName();
        var expectedResult = multipartFile.getName();
        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] 파일 다건 조회")
    void findAllByIdIn() throws IOException {
        //given
        MultipartFile multipartFile1 = new MockMultipartFile(
                "file1",
                "dummy.txt",
                "application/octet-stream",  // 일반적으로 그냥 placeholder로 사용 가능
                new byte[0]
        );
        MultipartFile multipartFile2 = new MockMultipartFile(
                "file2",
                "dummy.txt",
                "application/octet-stream",  // 일반적으로 그냥 placeholder로 사용 가능
                new byte[0]
        );
        MultipartFile multipartFile3 = new MockMultipartFile(
                "file3",
                "dummy.txt",
                "application/octet-stream",  // 일반적으로 그냥 placeholder로 사용 가능
                new byte[0]
        );
        UserDto user1 = userService.createUser(TestFixture.userCreateFactory(), multipartFile1);
        UserDto user2 = userService.createUser(TestFixture.userCreateFactory(), multipartFile2);
        UserDto user3 = userService.createUser(TestFixture.userCreateFactory(), multipartFile3);

        //when
        List<BinaryContentDto> allByIdIn = binaryContentService.findAllByIdIn(List.of(user1.profile().id(),
                user2.profile().id(),
                user3.profile().id()));

        //then
        var actualResult = allByIdIn;
        var expectedResult = List.of(binaryContentService.find(user1.profile().id()),
                binaryContentService.find(user2.profile().id()),
                binaryContentService.find(user3.profile().id()));
        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] 파일 전체 조회")
    void findAll() {
        var actualResult = binaryContentService.findAll();
        var expectedResult = binaryContentRepository.findAll();
        assertEquals(expectedResult.size(),actualResult.size());
    }

    @Test
    @DisplayName("[정상 케이스] 파일 다운로드" )
    void downloadFile() throws IOException {
        //given
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "dummy.txt",
                "application/octet-stream",  // 일반적으로 그냥 placeholder로 사용 가능
                new byte[0]
        );
        var userDto = userService.createUser(TestFixture.userCreateFactory(),multipartFile);

        //when
        ResponseEntity<?> responseEntity = binaryContentService.downloadFile(userDto.profile().id());

        //then
        var actualResult = binaryContentStorage.get(userDto.profile().id())!=null;
        assertEquals(true,actualResult);

    }

    @Test
    @DisplayName("[예외 케이스] 파일 값에 null 입력")
    void downloadNullFile() throws IOException {
        var userDto = userService.createUser(TestFixture.userCreateFactory(),null);

        assertThrows(NullPointerException.class,()->binaryContentService.downloadFile(userDto.profile().id()));

    }
}