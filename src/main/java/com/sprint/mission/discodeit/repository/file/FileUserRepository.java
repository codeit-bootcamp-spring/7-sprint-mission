package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.PrintUtil;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.lang.module.FindException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void deleteById(UUID id) {
        fileUtil.deleteRepository(id);
    }

    @Override
    public boolean isUsingName(String name) {
        boolean isExist = fileUtil.findAll().stream().map(user -> (User)user).anyMatch(user -> user.getUserName().equals(name));
        return isExist;
    }

    @Override
    public boolean isUsingEmail(String eMail) {
        boolean isExist = fileUtil.findAll().stream().map(user -> (User)user).anyMatch(user -> user.getEMail().equals(eMail));
        return isExist;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return fileUtil.findModel(id).map(user -> (User)user);
    }

    @Override
    public Optional<List<User>> findAll() {
        return Optional.ofNullable(fileUtil.findAll().stream().map(user -> (User)user).toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return fileUtil.existsRepository(id);
    }

    @Override
    public boolean existsByName(String thisName) {
        return fileUtil.findAll().stream().map(user-> (User)user).anyMatch(name -> name.getUserName().equals(thisName));
    }

    @Override
    //!! 🎓레포지토리에서 로그인 여부를 반환하는데요 레포지토리의 책임일지 고민을 해보실필요가있을거같습니다.
    public Res_UserLogin isLogin(String name, String password) {
        User user1 = fileUtil.findAll().stream()
                            .map(user -> (User) user)
                            .filter(user -> user.getUserName().equals(name) && user.getPassword().equals(password))
                            .findFirst()
                            .orElseThrow(() -> new FindException("AuthService.login.name = [" + name + "] 또는 password 오류"));
        PrintUtil.okMessage("AuthService.isLogin = [" + user1.getUserName() + "]");
        return Res_UserLogin.from(user1);
    }
}
