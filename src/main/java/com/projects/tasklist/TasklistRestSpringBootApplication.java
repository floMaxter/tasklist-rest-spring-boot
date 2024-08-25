package com.projects.tasklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TasklistRestSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasklistRestSpringBootApplication.class, args);
    }

}
