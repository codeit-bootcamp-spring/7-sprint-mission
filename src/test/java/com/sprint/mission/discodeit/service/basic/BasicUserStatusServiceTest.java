//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
//import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusUpdateRequestDto;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.Instant;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class BasicUserStatusServiceTest {
//
//    private static final Logger log = LoggerFactory.getLogger(BasicUserStatusServiceTest.class);
//    @Autowired
//    private BasicUserStatusService basicUserStatusService;
//
//    @Autowired
//    private UserStatusRepository userStatusRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.resetUserRepository();
//        userStatusRepository.resetRepository();
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 스테이터스 업데이트")
//    void updateByUserId() {
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 스테이터스 생성")
//    void createUserStatus() {
//        //given
//        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
//      var userStatus = basicUserStatusService.createUserStatus(new UserStatusCreateRequestDto(user.getId(), Instant.now()));
//
//      //when
//    var actualResult = userStatusRepository.readUserStatus(userStatus.getId()).orElseThrow();
//        //then
//        assertThat(actualResult.getId()).isEqualTo(userStatus.getId());
//    }
//
//    @Test
//    @DisplayName("[예외 케이스] - 이미 존재하는 유저 스테이터스 생성 에러 발생")
//    void createExistUserStatusError(){
//        //given
//        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
//        var userStatus = basicUserStatusService.createUserStatus(new UserStatusCreateRequestDto(user.getId(), Instant.now()));
//        //when & then
//        assertThrows(IllegalArgumentException.class, () -> basicUserStatusService.createUserStatus(new UserStatusCreateRequestDto(user.getId(), Instant.now())));
//    }
//    @Test
//    @DisplayName("[에러 케이스] - 존재하지 않는 유저 생성 에러")
//    void createNotExistUserError(){
//        //given
//        var userStatus = new UserStatusCreateRequestDto(UUID.randomUUID(), Instant.now());
//
//        //when & then
//        assertThrows(IllegalArgumentException.class, () -> basicUserStatusService.createUserStatus(userStatus));
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 스테이터스 삭제")
//    void deleteUserStatus() {
//        //given
//        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
//        var userStatus = basicUserStatusService.createUserStatus(new UserStatusCreateRequestDto(user.getId(), Instant.now()));
//
//        //when
//        basicUserStatusService.deleteUserStatus(userStatus.getId());
//        var actualResult = userStatusRepository.readAllUserStatus().stream().noneMatch(us -> us.getId().equals(userStatus.getId()));
//
//        //then
//        assertThat(actualResult).isTrue();
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 스테이터스 유저 id로 업데이트")
//    void updateUserStatus() {
//        //given
//        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
//        var userStatus = basicUserStatusService.createUserStatus(new UserStatusCreateRequestDto(user.getId(), Instant.now()));
//
//        //when
//        basicUserStatusService.updateUserStatus(new UserStatusUpdateRequestDto<>
//                (userStatus.getId(),
//                        UserStatusElement.LAST_ONLINE_TIME, Instant.MAX));
//        var actualResult = userStatusRepository.readUserStatus(userStatus.getId()).orElseThrow().getLastOnlineTime();
//        //then
//        assertThat(actualResult).isEqualTo(Instant.MAX);
//
//    }
//
//    @Test
//    @DisplayName("[정상 케이스] - 유저 스테이터스 조회")
//    void find() {
//        //given
//        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
//        var userStatus = basicUserStatusService.createUserStatus(new UserStatusCreateRequestDto(user.getId(), Instant.now()));
//
//        //when
//        var actualResult = userStatusRepository.readAllUserStatus().stream().anyMatch(us -> us.getId().equals(userStatus.getId()));
//        //then
//        assertThat(actualResult).isTrue();
//    }
//}