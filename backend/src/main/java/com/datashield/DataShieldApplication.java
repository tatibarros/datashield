package com.datashield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataShieldApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataShieldApplication.class, args);
    }
}
