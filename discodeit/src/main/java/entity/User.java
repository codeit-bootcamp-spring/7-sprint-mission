package entity;

public class User extends BaseEntity {
    private final String accountId;
    private String password;
    private String email;
    private String phone;
    private String nickname;
    private String introduce;

    public User(String accountId, String password, String email, String phone, String nickname, String introduce) {
        super();
        this.accountId = accountId;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.introduce = introduce;

    }

    public String getAccountId() {
        return accountId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
        touch();
    }

    public void changePassword(String password) {
        this.password = password;
        touch();
    }
}