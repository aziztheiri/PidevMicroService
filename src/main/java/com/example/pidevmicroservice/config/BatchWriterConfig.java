package com.example.pidevmicroservice.config;

import com.example.pidevmicroservice.dto.UserReportDTO;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchWriterConfig {

    @Bean
    public FlatFileItemWriter<UserReportDTO> userReportItemWriter() {
        FlatFileItemWriter<UserReportDTO> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("user-report.csv"));

        // Configurer l'agrégateur de ligne avec une virgule comme séparateur
        DelimitedLineAggregator<UserReportDTO> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<UserReportDTO> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {
                "cin", "email", "name", "userRole", "isVerified", "creationDate", "age", "gender"
        });
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);
        return writer;
    }
}
