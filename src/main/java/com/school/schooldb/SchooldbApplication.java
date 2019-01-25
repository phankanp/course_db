package com.school.schooldb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SchooldbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchooldbApplication.class, args);
    }

}

