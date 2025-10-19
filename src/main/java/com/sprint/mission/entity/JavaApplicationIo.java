package com.sprint.mission.entity;

import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.UUID;


public class JavaApplicationIo {
    public static void main(String[] args) {

        FileUserService JCFUserRepository = FileUserService.getInstance();//유저기능
        //FileChannelService fileChannelService = new FileChannelService();


     //중복이면 안넣는코드를 넣어야 하는데
        // 조회하고 다시 넣고가 좀 그렇지
     // fileUserService.create("jam@ewew", "123123", "신제원", "바보");

        User u0 = JCFUserRepository.create("jam@ewew", "123123", "신제원", "바보");
        UUID u1 = u0.getId();

        User u2 =   JCFUserRepository.create("kim@test.com", "abc123", "김철수", "철수짱");
       User u3 =  JCFUserRepository.create("lee@sample.com", "pass456", "이영희", "히히걸");
        User u4 =   JCFUserRepository.create("park@hello.com", "qwerty", "박민수", "민수킹");
        User u5 =  JCFUserRepository.create("choi@demo.com", "pw7890", "최지우", "지우스타");

        //조회
       JCFUserRepository.read(u1);
        JCFUserRepository.readAll().forEach(System.out::println);

        //수정

        JCFUserRepository.update(u1, u-> {
            u.setUserId("dd");
            u.setUserNickname("d");
        });

 //조회
        System.out.println(JCFUserRepository.read(u1));
        //삭제
        JCFUserRepository.delete(u1);
        //조회
        JCFUserRepository.read(u1);
        JCFUserRepository.readAll().forEach(System.out::println);


    }
}
