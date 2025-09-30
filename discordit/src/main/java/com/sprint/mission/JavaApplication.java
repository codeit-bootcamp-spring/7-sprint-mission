package com.sprint.mission;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.jcf.JCFChannelService;
import com.sprint.mission.service.jcf.JCFMessageService;
import com.sprint.mission.service.jcf.JCFUserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;

public class JavaApplication {

    public static final JCFMessageService<Channel> channelMessageService = new JCFMessageService<>();
    public static final JCFMessageService<User> userMessageService = new JCFMessageService<>();
    public static final JCFUserService userService = new JCFUserService();
    public static final JCFChannelService channelService = new JCFChannelService();

    public static void main(String[] args) throws InterruptedException {
        userService.signIn("nbh", "1234", "nbh");
        userService.signIn("yoon", "npnp1234", "yoonna");
        userService.signIn("jis121", "gaalgals", "didu");
        userService.signIn("ming11", "unggui", "ugcute");

        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            System.out.println(user);
        }

        printLine();

        System.out.println(userService.getUserById("nbh"));
        userService.setBio("yoon", "I love drawing");
        userService.setBio("ming11", "work in tokyo");
        System.out.println(userService.getUsers("yoon", "ming11"));

        waitSeconds(3);


        userService.signIn("happypancake", "cakehappy", "heeyeon");
        userService.setBio("happypancake", "I'm cute heeyeon");

        printLine();

        allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            System.out.printf("id\t : %s\n" +
                            "Name\t : %s\n" +
                            "status\t : %s\n" +
                            "created\t : %s\n" +
                            "Updated\t : %s\n\n",
                    user.getUserId(),
                    user.getDisplayName(),
                    user.getOnlineStatus(),
                    timeFormatter(user.getCreatedAt()),
                    timeFormatter(user.getUpdatedAt()));
        }

        printLine();

        allUsers = userService.getAllUsers();
        System.out.println("allUsers.size() = " + allUsers.size());

        userService.deleteUser("happypancake");
        allUsers = userService.getAllUsers();
        System.out.println("allUsers.size() = " + allUsers.size());

        printLine();

        userService.setOnlineStatus("yoon", User.Status.ONLINE);
        System.out.println(userService.getUserById("yoon").getOnlineStatus());


    }

    public static String timeFormatter(long unixTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixTime),
                ZoneId.systemDefault()
        );

        // 한국어 스타일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
//        System.out.println(dateTime.format(formatter));
        // 출력: 2024년 03월 15일 14:30:45
        return dateTime.format(formatter);
    }

    public static void printLine() {
        System.out.println("=====================================");
    }

    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000); // 밀리초 단위
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
            System.out.println("대기가 중단되었습니다.");
        }
    }


}
