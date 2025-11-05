package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.Util;
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

//    public User(String name, String password, String email, UUID profileId) {
//        super();
//        this.username = name;
//        this.password = password;
//        this.email = email;
//        this.profileId = profileId;
//    }

    public User(Dto_User dtoUser, UUID profileId) {
        super();
        this.userName = dtoUser.userName();
        this.password = dtoUser.password();
        this.eMail = dtoUser.eMail();
        this.profileId = profileId;

        switch (dtoUser.userName()) { //!! for test
            case "여우" : setIdForTest(UUID.fromString("ae3d209c-b2be-41d7-895e-30bb4192498c")); break;
            case "🐶강아지" : setIdForTest(UUID.fromString("abc5103d-d916-4ea2-8b45-22be27f66b2f")); break;
            case "🐼팬더" : setIdForTest(UUID.fromString("ceb51b81-cb4a-41b1-b8f8-3dd15bdbedd9")); break;
            case "🐯호랑이" : setIdForTest(UUID.fromString("f84daca8-d311-4858-bd01-7693adf65c02")); break;
            case "🦁사자" : setIdForTest(UUID.fromString("496859e7-1536-4ad7-bb64-ba895f756288")); break;
            default : break;
        }
        Util.okMessage(dtoUser.userName() + ".readStatusID = [" + this.getId() + "]");
    }

    @Override
    public String toString() {
        String strProfileId = (profileId == null) ? "no profile" : profileId.toString();
        return "user {" +
                super.toString() +
                "\n name = [" + userName + "] " +
                "\n password = [" + password + "]"   +
                "\n email = [" + eMail + "]"   +
                "\n profileId = [" + strProfileId + "]" + //❌ 생성자에서 제외
                "}";
    }

    public void updateUser(String reName, String password, String reEmail, UUID profiledId) {
//        reName.isPresent(strName -> this.username = reName);
//        reEmail.ifPresent(strEmail -> this.email = reEmail);
        if (reName != null) this.userName = reName;
        if (password != null) this.password = password;
        if (reEmail != null) this.eMail = reEmail;
        if (profiledId != null) this.profileId = profiledId;
        super.setUpdatedAt();
    }
}