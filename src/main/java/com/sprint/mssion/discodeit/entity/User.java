package com.sprint.mssion.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User{
    private final Common common;
    private String username;
    private String email;
    private String phoneNumbers;
    private String pronoun; // 대명사 (디스코드에 있음)
    private final List<UUID> joinChannels = new ArrayList<>(); // 현재 참여중인 채널 리스트(UUID 리스트)

    public User (String username, String email, String phoneNumbers, String pronoun) {
        this.username = username;
        this.email = email;
        this.phoneNumbers = phoneNumbers;
        this.pronoun = pronoun;
        this.common = new Common();
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getPronoun() {
        return pronoun;
    }

    public Common getCommon() {
        return common;
    }

    public List<UUID> getJoinChannels() {
        return joinChannels;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public void addChannel(UUID channelID){
        if(!joinChannels.contains(channelID)){
            joinChannels.add(channelID);
        }
    }
    public void removeChannel(UUID channelID){
        joinChannels.remove(channelID);
    }

    @Override
    public String toString() {
        return "User{" +
                "common=" + common +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                ", pronoun='" + pronoun + '\'' +
                ", joinChannels=" + joinChannels +
                '}';
    }
}
