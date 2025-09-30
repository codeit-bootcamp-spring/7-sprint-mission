package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryUserRepository {

    //자바에서 final이 붙는 건 참조 자체를 변경할 수 없게 만든다는 뜻
    //그래서 리스트 안에 있는 내용은 수정 가능
    private final Map<UUID, User> store = new HashMap<>();

    public void save(User user){
        if(user.getUsername()==null || user.getPassword()==null || user.getEmail()==null||user.getPhoneNumber()==null) {
            System.out.println("입력이 누락되었음");
        } else{
            UUID key = user.getId();
            store.put(key, user);
            System.out.println("유저 저장 성공");
        }
    }

    public void remove(User user){
        UUID userId = user.getId();
        if (store.containsKey(userId)){
            store.remove(userId);
            System.out.println("유저 삭제 성공");
        } else {
            System.out.println("유저 정보 없음 -> 삭제 실패");
        }
    }

    public User findById(UUID id) {
        if (store.containsKey(id)) {
            System.out.println("유저 찾기 성공");
            return store.get(id);
        } else {
            System.out.println("유저 정보 없음 -> 수정 실패");
            return null;
        }
    }

    public List<User> findAll(){
        return store.values().stream().toList();
    }

    public void updateUser(UUID id, UserDto userDto){
        User user = store.get(id);
        if(user ==null){
            System.out.println("유저 정보 없음 -> 찾기 실패");
            return;
        }
        if (userDto.getEmail()!=null){
            System.out.println("이메일 변경 성공");
            user.setEmail(userDto.getEmail());
        }
        if(userDto.getNickname()!=null){
            System.out.println("닉네임 변경 성공");
            user.setNickname(userDto.getNickname());
        }
        if(userDto.getPassword()!=null){
            System.out.println("비밀번호 변경 성공");
            user.setPassword(user.getPassword());
        }
        if(userDto.getPhoneNumber()!=null){
            System.out.println("번호 변경 성공");
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        if (userDto.getUsername()!=null){
            System.out.println("이름 변경 성공");
            user.setUsername(userDto.getUsername());
        }

    }



}
