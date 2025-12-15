package com.example.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class MissionAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MissionAdminApplication.class, args);
    }

}
