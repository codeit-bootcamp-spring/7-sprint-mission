package com.sprint.mission.entity;

import com.sprint.mission.discodeit.service.file.FileUserService;


public class JavaApplicationIo {
    public static void main(String[] args) {

        FileUserService fileUserService = FileUserService.getInstance();//유저기능
        //FileChannelService fileChannelService = new FileChannelService();



       fileUserService.create("jam@ewew", "123123", "신제원", "바보");
    }
}
