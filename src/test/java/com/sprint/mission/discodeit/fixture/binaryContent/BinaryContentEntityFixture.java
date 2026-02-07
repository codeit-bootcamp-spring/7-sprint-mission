package com.sprint.mission.discodeit.fixture.binaryContent;

import com.sprint.mission.discodeit.entity.User;

public class BinaryContentEntityFixture {
    public static User createDefault() {
        return new User(
                "testUser",
                "test@email.com",
                "testPassword",
                null
        );
    }
}
