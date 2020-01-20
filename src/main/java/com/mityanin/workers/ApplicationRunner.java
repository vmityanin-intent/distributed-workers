package com.mityanin.workers;

import com.mityanin.workers.config.ApplicationConfig;
import com.mityanin.workers.service.util.DummyDataPopulator;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableBatchProcessing
@EnableAsync
@Import(ApplicationConfig.class)
public class ApplicationRunner {
    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(ApplicationRunner.class);
        DummyDataPopulator.populate(ctx);
    }
}
