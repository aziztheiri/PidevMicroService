package com.example.pidevmicroservice.config;

import com.example.pidevmicroservice.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchReaderConfig {

    @Bean
    public JpaPagingItemReader<User> userItemReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT u FROM User u");
        reader.setPageSize(100);
        return reader;
    }
}