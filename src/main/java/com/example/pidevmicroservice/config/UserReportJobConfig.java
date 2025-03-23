package com.example.pidevmicroservice.config;

import com.example.pidevmicroservice.dto.UserItemProcessor;
import com.example.pidevmicroservice.dto.UserReportDTO;
import com.example.pidevmicroservice.entities.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class UserReportJobConfig {

    @Bean
    public Job userReportJob(JobRepository jobRepository, Step userReportStep) {
        return new JobBuilder("userReportJob", jobRepository)
                .start(userReportStep)
                .build();
    }

    @Bean
    public Step userReportStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               JpaPagingItemReader<User> userItemReader,
                               UserItemProcessor userItemProcessor,
                               FlatFileItemWriter<UserReportDTO> userReportItemWriter) {
        return new StepBuilder("userReportStep", jobRepository)
                .<User, UserReportDTO>chunk(100, transactionManager)
                .reader(userItemReader)
                .processor(userItemProcessor)
                .writer(userReportItemWriter)
                .build();
    }
}
