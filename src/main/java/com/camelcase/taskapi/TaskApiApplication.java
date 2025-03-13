package com.camelcase.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.camelcase.taskapi", "com.camelcase.taskapi.controller"})
//@ComponentScan(basePackages = {"com.camelcase.taskapi.controller"})  // Explicitly scan the controller package
public class TaskApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApiApplication.class, args);
    }
}
