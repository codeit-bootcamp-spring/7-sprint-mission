package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.AuthServiceDto;
import com.sprint.mission.discodeit.dto.Res_UserLogin;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import java.lang.module.FindException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class FileUserRepository implements InterfaceUserRepository {
    private final FileUtil fileUtil;

    public FileUserRepository(@Qualifier("userFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(User user) {
        fileUtil.saveRepository(user);
    }

    @Override
    public boolean deleteById(UUID id) {
        return fileUtil.deleteRepository(id);
    }

    @Override
    public boolean isUsingName(String name) {
        boolean isExist = fileUtil.findAll().stream().map(user -> (User)user).anyMatch(user -> user.getUsername().equals(name));
        return isExist;
    }

    @Override
    public boolean isUsingEmail(String eMail) {
        boolean isExist = fileUtil.findAll().stream().map(user -> (User)user).anyMatch(user -> user.getEmail().equals(eMail));
        return isExist;
    }

  @Override
  public Optional<User> findByName(String name) {
        return fileUtil.findAll().stream().map(user -> (User) user)
        .filter(user -> user.getUsername().equals(name))
        .findFirst();
  }

  @Override
    public Optional<User> findById(UUID id) {
        return fileUtil.findModel(id).map(user -> (User)user);
    }

    @Override
    public List<User> findAll() {
        return fileUtil.findAll().stream().map(user -> (User)user).toList();
    }

    @Override
    //!! 🎓레포지토리에서 로그인 여부를 반환하는데요 레포지토리의 책임일지 고민을 해보실필요가있을거같습니다.
    public Res_UserLogin isLogin(AuthServiceDto authServiceDto) {
        User user1 = fileUtil.findAll().stream()
                            .map(user -> (User) user)
                            .filter(user -> user.getUsername().equals(authServiceDto.username()) && user.getPassword().equals(authServiceDto.password()))
                            .findFirst()
                            .orElseThrow(() -> new FindException("AuthService.login.name = [" + authServiceDto.username() + "] 또는 newPassword 오류"));
        log.info("✅ AuthService.login = [" + user1.getUsername() + "]");
        return Res_UserLogin.from(user1);
    }
}
