package com.demo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.annotation.ApplicationScope;

@SpringBootApplication
@ApplicationScope
public class VideoStorageApplication {

  public static void main(String[] args) {
    SpringApplication.run(VideoStorageApplication.class, args);
  }
}
