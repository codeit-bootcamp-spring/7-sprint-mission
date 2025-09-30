package com.sprint.mission.discodeit.entity;

public final class VerifiedUtils {
    public static String verifyName(String name) {
        if (name == null || name.isBlank()) {throw new IllegalArgumentException("username cannot be null");}
        String s = name.trim();
        if (s.length() > 20 || s.length() < 2) { throw new IllegalArgumentException("username length must be between 2 and 20 characters");}
        return s;
    }

    public static String verifyPassword(String password) {
        if(password == null || password.isBlank()) {throw new IllegalArgumentException("password cannot be null");}
        if(password.length() > 50 || password.length() < 7) {throw new IllegalArgumentException("password length must be between 50 and 7");}
        return password;
    }

    public static String verifyEmail(String email) {
        if (email == null || email.isBlank()) {throw new IllegalArgumentException("email cannot be null");}
        String s = email.trim().toLowerCase();
        if(!s.contains("@")) { throw new IllegalArgumentException("email must contain '@'");}
        return s;
    }

    public static String verifyContent(String content) {
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("content cannot be null or blank");
        }
        String s = content.trim();
        if(s.length() < 2 || s.length() > 100) {
            throw new IllegalArgumentException("content length must be between 2 and 100 characters");
        }
        return s;
    }
}
