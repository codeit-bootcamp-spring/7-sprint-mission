package com.sprint.mission.discodeit.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void 친구_요청_보내고_수락() {
        User user1 = new User("test@email.com", "1234", "ian", "010-3030-3030");
        User user2 = new User("test2@email.com", "1234", "wi", "010-3030-1234");

        user1.sendFriendInvitation(user2);
        user2.acceptInvitation(user1);
        List<UUID> receivedInvitations = user2.getReceivedInvitations();
        List<UUID> friends = user1.getFriends();

        assertThat(receivedInvitations).contains(user1.getId());
        assertThat(friends).contains(user2.getId());


    }
}