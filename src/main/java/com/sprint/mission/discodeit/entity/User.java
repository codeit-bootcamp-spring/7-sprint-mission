package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseModel {
    private UUID profileId; // for BinaryContent
    private String userName;
    private String password;
    private String eMail;

    public User(String name, String password, String eMail, UUID profiledId) {
        super();
        this.userName = name;
        this.password = password;
        this.eMail = eMail;
        this.profileId = profiledId;
    }

    @Override
    public String toString() {
        String strProfileId = (profileId == null) ? "no profile" : profileId.toString();
        return "user {" +
                super.toString() +
                "\n name = [" + userName + "] " +
                "\n password = [" + password + "]"   +
                "\n eMail = [" + eMail + "]"   +
                "\n profiledId = [" + strProfileId + "]" + //❌ 생성자에서 제외
                "}";
    }

    public void updateUser(String reName, String password, String reEmail, UUID profiledId) {
//        reName.isPresent(strName -> this.userName = reName);
//        reEmail.ifPresent(strEmail -> this.eMail = reEmail);
        if (reName != null) this.userName = reName;
        if (password != null) this.password = password;
        if (reEmail != null) this.eMail = reEmail;
        if (profiledId != null) this.profileId = profiledId;
        super.setUpdatedAt();
    }
}