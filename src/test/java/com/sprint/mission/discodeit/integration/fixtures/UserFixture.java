package com.sprint.mission.discodeit.integration.fixtures;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.repository.UserRepository;

import java.nio.charset.StandardCharsets;

public final class UserFixture {

    private UserFixture() {
    } // 인스턴스 생성 방지

    public static User createUser(UserRepository userRepository) {
        return userRepository.save(defaultUser());
    }

    public static User createUser(User user, UserRepository userRepository) {
        return userRepository.save(user);
    }

    public static User createUserWithStatus(UserRepository userRepository
                                           ) {
        User user = defaultUser();
        user.initUserStatus();      // 양방향 세팅
        userRepository.save(user);
        return user;
    }

    public static User createUserWithStatus(User user, UserRepository userRepository) {
        user.initUserStatus();// 양방향 세팅
        userRepository.save(user);
        return user;
    }

    public static User defaultUser() {
        byte[] payload = "fake-bytes".getBytes(StandardCharsets.UTF_8);
        BinaryContent binaryContent = new BinaryContent("profile.png", "image/png", (long) payload.length);
        return User.builder()
                .nickname("name")
                .email("ee@exam.com")
                .password("dsfsdfdf")
                .profile(binaryContent)
                .build();
    }
}