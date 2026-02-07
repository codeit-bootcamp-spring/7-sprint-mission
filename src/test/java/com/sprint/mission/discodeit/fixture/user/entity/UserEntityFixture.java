package com.sprint.mission.discodeit.fixture.user.entity;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserEntityFixture {
    public static User createUser(String username) {
        return new User(
                username,
                username + "@codeit.com",
                "password123",
                null
        );
    }

    public static List<User> createUser(int num, String username) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            users.add(createUser(username + i));
        }
        return users;
    }
}
