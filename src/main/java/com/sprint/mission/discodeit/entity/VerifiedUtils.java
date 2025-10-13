package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public final class VerifiedUtils {
    public static String verifyName(String name) {
        String n = verifyNullOrBlank(name);
        if (n.length() > 20 || n.length() < 2) {
            throw new IllegalArgumentException("name length must be between 2 and 20 characters");
        }
        return n;
    }

    public static String verifyPassword(String password) {
        String n = verifyNullOrBlank(password);
        if(password.length() < 7 || password.length() > 50) {
            throw new IllegalArgumentException("password length must be between 7 and 50 characters");
        }
        return password;
    }

    public static String verifyEmail(String email) {
        String n = verifyNullOrBlank(email);
        String s = n.trim().toLowerCase();
        if(!s.contains("@")) {
            throw new IllegalArgumentException("email must contain '@'");
        }
        return s;
    }

    public static String verifyContent(String content) {
        String n = verifyNullOrBlank(content);
        if(n.length() < 2 || n.length() > 100) {
            throw new IllegalArgumentException("content length must be between 2 and 100 characters");
        }
        return n;
    }

    public static <T> T verifyNull(T value)
    {
        if(value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        return value;
    }

//    public static <T> T verifyNull(T value, String filedName)
//    {
//        if(value == null) throw new IllegalArgumentException(filedName + "value cannot be null");
//        return value;
//    }

    public static String verifyNullOrBlank(String value)
    {
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("value cannot be null or blank");
        }
        return value.trim();
    }
}
