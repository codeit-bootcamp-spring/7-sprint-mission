package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private final String email;         //이메일
    private String nickname;            //닉네임
    private String password;            //비밀번호

    //Constructor
    public User(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    //update Nickname
    public User update(String nickname, String password) {
        super.update();
        this.nickname = nickname;
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword(){
        return password;
    }
}
