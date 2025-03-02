package ru.goncharenko.aitor.configuration;

import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration {
    @Bean
    public MongoOperations mongoTemplate(MongoClient client) {
        return new MongoTemplate(client, "aitor");
    }
}
