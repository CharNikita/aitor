package ru.goncharenko.aitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AitorApplication.class, args);
    }
}
