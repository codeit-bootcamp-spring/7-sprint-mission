package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Common common;
    private String username;
    private String email;
    private String phoneNumber;
    private String pronoun; // 대명사 (디스코드에 있음)
    private final List<UUID> joinChannels = new ArrayList<>(); // 현재 참여중인 채널 리스트(UUID 리스트)

    public User (String username, String email, String phoneNumber, String pronoun) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pronoun = pronoun;
        this.common = new Common();
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public void addChannel(UUID channelID){
        if(!joinChannels.contains(channelID)){
            joinChannels.add(channelID);
            common.touch();
        }
    }
    public void removeChannel(UUID channelID){
        joinChannels.remove(channelID);
        common.touch();
    }

    @Override
    public String toString() {
        return "User{" +
                "common=" + common +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", pronoun='" + pronoun + '\'' +
                ", joinChannels=" + joinChannels +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(common, user.common) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(pronoun, user.pronoun) && Objects.equals(joinChannels, user.joinChannels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(common, username, email, phoneNumber, pronoun, joinChannels);
    }
}
