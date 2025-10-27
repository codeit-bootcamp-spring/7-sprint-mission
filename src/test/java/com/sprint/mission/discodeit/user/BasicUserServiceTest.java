package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.application.BasicUserService;

public class BasicUserServiceTest {


    private BasicUserService basicUserService;

//    @BeforeEach
//    void beforeAll(){
//        this.basicUserService =
//                new BasicUserService(new MemoryUserRepository(), new MemoryFriendRequestRepository(), new MemoryFriendShipRepository());
//    }
//
//
//    @Test
//    void 회원가입_성공() {
//        // given
//        User user = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
//
//        // when
//        basicUserService.register(user);
//
//        // then
//        User found = basicUserService.findByEmail("test@example.com");
//        assertThat(user).isEqualTo(found);
//    }
//
//    @Test
//    void 중복가입_예외발생() {
//        // given
//        User user1 = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
//        User user2 = User.create("test@example.com", "4321", "Tanos", "010-1234-9876");
//        basicUserService.register(user1);
//
//        // when & then
//        assertThatThrownBy(()-> basicUserService.register(user2))
//                .isInstanceOf(DuplicateUserException.class);
//    }
//
//    @Test
//    void 유저삭제_성공() {
//        // given
//        User user = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
//        basicUserService.register(user);
//
//        // when
//        basicUserService.remove(user);
//
//        // then
//        assertThatThrownBy(()-> basicUserService.findById(user.getId())).isInstanceOf(NoSuchElementException.class);
//    }
//
//    @Test
//    void 친구_요청_보내기_성공(){
//        //given
//        User user1 = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
//        User user2 = User.create("test2@example.com", "1234", "Cha", "010-4321-5678");
//        basicUserService.register(user1);
//        basicUserService.register(user2);
//
//        //when
//        basicUserService.sendFriendRequest(user1,user2);
//
//        //then
//        List<FriendRequest> receivedRequests = basicUserService.getReceivedFriendRequests(user2);
//        assertThat(receivedRequests.size()).isEqualTo(1);
//
//    }

}

