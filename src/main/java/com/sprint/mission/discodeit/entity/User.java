package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.vo.Invitation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private final List<UUID> myMessageRooms = new ArrayList<>();
    private final List<Invitation> myInvitations =new ArrayList<>();
    private final List<UUID> myChannels = new ArrayList<>();
    private final List<UUID> friends = new ArrayList<>();




    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }


    public void setPassword(String password) {
        if (password == null || password.length() < 8) {
            System.out.println("비밀번호를 8글자 이상 적어주세요.");
        } else {
            this.password = password;
        }
    }

    public void setEmail(String email) {
        if (email == null || email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
            System.out.println("올바른 이메일 형식으로 적어주세요");
        } else {
            this.email = email;
        }

    }

    public void updateUsername(String newUsername){
        this.updatedAt=System.currentTimeMillis();
        this.username=newUsername;
    }
    public void updatePassword(String newPassword){
        if (newPassword == null || newPassword.length() < 8) {
            System.out.println("비밀번호를 8글자 이상 적어주세요.");
            return;
        }
        this.updatedAt=System.currentTimeMillis();
        this.password=newPassword;
    }
    public void updateEmail(String newEmail){
        if ( email.indexOf('@') == -1 || email.indexOf('@') != email.lastIndexOf('@')) {
            System.out.println("올바른 이메일 형식으로 적어주세요");
        }
        this.updatedAt=System.currentTimeMillis();
        this.email=newEmail;
    }
    public void updateNickname(String newNickname){
        this.updatedAt=System.currentTimeMillis();
        this.nickname=newNickname;
    }
    public void updatePhoneNumber(String newPhoneNumber){
        this.updatedAt=System.currentTimeMillis();
        this.phoneNumber=newPhoneNumber;
    }


    public List<UUID> getMyMessageRooms() {
        return List.copyOf(myMessageRooms);
    }

    public List<UUID> getMyChannels() {
        return List.copyOf(myChannels);
    }

    public void addMyChannel(UUID uuid){
        myChannels.add(uuid);
    }

    public List<Invitation> getMyInvitations() {
        return List.copyOf(myInvitations);
    }

    public void addMyInvitation(Invitation invitation){
        myInvitations.add(invitation);
    }

    public void removeMyInvitation(Invitation invitation){
        myInvitations.remove(invitation);
    }

    public List<UUID> getFriends() {
        return List.copyOf(friends);
    }

    public void addFriend(UUID id){
        friends.add(id);
    }

}
