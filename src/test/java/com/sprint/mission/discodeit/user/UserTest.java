package com.sprint.mission.discodeit.user;


import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class UserTest {

    @Test
    void 정상적인_유저_생성() {
        // given
        String email = "test@example.com";
        String password = "abcd1234";
        String username = "Ian";
        String phoneNumber = "010-1234-5678";

        // when
        User user = User.create(email, password, username, phoneNumber);

        // then

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void 이메일_형식이_잘못되면_예외발생() {
        // given
        String wrongEmail = "wrong-email";

        // when & then

        assertThatThrownBy(() -> User.create(wrongEmail, "abcd1234", "Ian", "010-1234-5678"))
                .isInstanceOf(ValidationException.class);

    }

    @Test
    void 비밀번호가_4자리_미만이면_예외발생() {
        assertThatThrownBy(() ->
                User.create("test@example.com", "123", "Ian", "010-1234-5678")
        ).isInstanceOf(ValidationException.class);

    }

    @Test
    void 이름이_null이거나_공백이면_예외발생() {
        assertThatThrownBy(() ->
                User.create("test@example.com", "abcd1234", " ", "010-1234-5678")
        ).isInstanceOf(ValidationException.class);
    }

    @Test
    void 전화번호_형식이_잘못되면_예외발생() {
        assertThatThrownBy(() ->
                User.create("test@example.com", "abcd1234", "Ian", "010-12-5678")
        ).isInstanceOf(ValidationException.class);
    }


    @Test
    void 이메일_정상_수정_성공() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");
        long beforeUpdate = user.getUpdatedAt();

        // when
        user.updateEmail("new@email.com");

        // then
        assertThat(user.getEmail()).isEqualTo("new@email.com");

    }

    @Test
    void 이메일_잘못된_형식이면_예외발생() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");

        // when & then
        assertThatThrownBy(() -> user.updateEmail("wrong-email"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void 비밀번호_정상_수정_성공() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");
        long beforeUpdate = user.getUpdatedAt();

        // when

        user.updatePassword("newPass123");

        // then
        assertThat(user.getPassword()).isEqualTo("newPass123");
    }

    @Test
    void 비밀번호_4자리_미만이면_예외발생() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");

        // when & then
        assertThatThrownBy(() -> user.updatePassword("123"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void 사용자이름_정상_수정_성공() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");
        long beforeUpdate = user.getUpdatedAt();

        // when
        user.updateUsername("Neo");

        // then
        assertThat(user.getUsername()).isEqualTo("Neo");

    }

    @Test
    void 사용자이름_빈값이면_예외발생() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");

        // when & then
        assertThatThrownBy(() -> user.updateUsername(" "))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void 닉네임_수정_성공() {
        // given
        User user = User.create("test@email.com", "abcd1234", "Ian", "010-1234-5678");
        long beforeUpdate = user.getUpdatedAt();

        // when
        user.updateNickname("아이언");

        // then
        assertThat(user.getNickname()).isEqualTo("아이언");

    }
}

