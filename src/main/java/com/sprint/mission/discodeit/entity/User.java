package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_UserCreate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User extends BaseModel {
    private String userName;
    private String password;
    private String eMail;
    private UUID profileId;

    public User(Dto_UserCreate dtoUser, UUID profileId) {
        super();
        this.userName = dtoUser.username();
        this.password = dtoUser.password();
        this.eMail = dtoUser.email();
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        String strProfileId = (profileId == null) ? "null" : profileId.toString();
        return "user {" +
                super.toString() +
                "\n name = [" + userName + "] " +
                "\n newPassword = [" + password + "]"   +
                "\n newEmail = [" + eMail + "]"   +
                "\n profileId = [" + strProfileId + "]" + //❌ 생성자에서 제외
                "}";
    }

    public void updateUser(String reName, String password, String reEmail, UUID profiledId) {
        if (reName != null) this.userName = reName;
        if (password != null) this.password = password;
        if (reEmail != null) this.eMail = reEmail;
        if (profiledId != null) this.profileId = profiledId;
        super.setUpdatedAtNow();
    }
}