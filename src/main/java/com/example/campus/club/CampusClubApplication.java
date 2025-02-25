package com.example.campus.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.example.campus.club.mapper")
public class CampusClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusClubApplication.class, args);
    }

}
