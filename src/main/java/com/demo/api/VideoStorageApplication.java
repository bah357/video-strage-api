package com.demo.api;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.annotation.ApplicationScope;

@EnableMongock
@SpringBootApplication
@ApplicationScope
public class VideoStorageApplication {

  public static void main(String[] args) {
    SpringApplication.run(VideoStorageApplication.class, args);
  }
}
