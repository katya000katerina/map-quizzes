package com.mapquizzes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MapQuizzesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapQuizzesApplication.class, args);
    }

}
