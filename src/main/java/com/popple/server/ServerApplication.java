package com.popple.server;

import com.popple.server.domain.entity.Event;
import com.popple.server.domain.event.service.EventService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
