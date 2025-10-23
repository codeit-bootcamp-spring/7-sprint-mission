package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;
import com.sprint.mission.discodeit.friendrequest.infrastructure.MemoryFriendRequestRepository;
import com.sprint.mission.discodeit.friendship.infrastructure.MemoryFriendShipRepository;
import com.sprint.mission.discodeit.user.application.BasicUserService;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.exception.DuplicateUserException;
import com.sprint.mission.discodeit.user.infrastructure.MemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

public class BasicUserServiceTest {


    private BasicUserService userService;

    @BeforeEach
    void beforeAll(){
        this.userService =
                new BasicUserService(new MemoryUserRepository(), new MemoryFriendRequestRepository(), new MemoryFriendShipRepository());
    }


    @Test
    void 회원가입_성공() {
        // given
        User user = User.create("test@example.com", "1234", "Ian", "010-1234-5678");

        // when
        userService.register(user);

        // then
        User found = userService.findByEmail("test@example.com");
        assertThat(user).isEqualTo(found);
    }

    @Test
    void 중복가입_예외발생() {
        // given
        User user1 = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
        User user2 = User.create("test@example.com", "4321", "Tanos", "010-1234-9876");
        userService.register(user1);

        // when & then
        assertThatThrownBy(()->userService.register(user2))
                .isInstanceOf(DuplicateUserException.class);
    }

    @Test
    void 유저삭제_성공() {
        // given
        User user = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
        userService.register(user);

        // when
        userService.remove(user);

        // then
        assertThatThrownBy(()->userService.findById(user.getId())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 친구_요청_보내기_성공(){
        //given
        User user1 = User.create("test@example.com", "1234", "Ian", "010-1234-5678");
        User user2 = User.create("test2@example.com", "1234", "Cha", "010-4321-5678");
        userService.register(user1);
        userService.register(user2);

        //when
        userService.sendFriendRequest(user1,user2);

        //then
        List<FriendRequest> receivedRequests = userService.getReceivedFriendRequests(user2);
        assertThat(receivedRequests.size()).isEqualTo(1);

    }

}

