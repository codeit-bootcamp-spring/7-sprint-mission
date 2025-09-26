package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String  password;
    private String email;
    private String nickname;
    private String username;
    private String phoneNumber;
    private Language language;
    private final UUID accountCode;

//    private List<User> friends;



    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.accountCode = UUID.randomUUID();
    }


    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Language getLanguage() {
        return language;
    }

    public UUID getAccountCode() {
        return accountCode;
    }
}
