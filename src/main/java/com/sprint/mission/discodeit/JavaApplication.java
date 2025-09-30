package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static final Map<UUID, User> userData = new HashMap<>();
    public static void main(String[] args) {

        System.out.println("Hello, Discodeit!");
        System.out.println("------------------------------------------------");

    }
}
