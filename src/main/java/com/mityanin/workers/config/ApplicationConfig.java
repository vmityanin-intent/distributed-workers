package com.mityanin.workers.config;

import com.mityanin.workers.service.BatchService;
import com.mityanin.workers.service.RestService;
import com.mityanin.workers.service.impl.DefaultBatchService;
import com.mityanin.workers.service.impl.DefaultRestService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(JobConfiguration.class)
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RestService getRestService(RestTemplate restTemplate) {
        return new DefaultRestService(restTemplate);
    }

    @Bean
    public BatchService getBatchService(JobLauncher jobLauncher, Job job) {
        return new DefaultBatchService(jobLauncher, job);
    }

}
