package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class User extends BaseModel {
    private String userName;
    private String password;
    private String eMail;
    private UUID profileId; // for BinaryContent

//    public User(String name, String password, String eMail, UUID profiledId) {
//        super();
//        this.userName = name;
//        this.password = password;
//        this.eMail = eMail;
//        this.profileId = profiledId;
//    }

    public User(Dto_User dtoUser, UUID profileId) {
        super();
        this.userName = dtoUser.userName();
        this.password = dtoUser.password();
        this.eMail = dtoUser.eMail();
        this.profileId = profileId;
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