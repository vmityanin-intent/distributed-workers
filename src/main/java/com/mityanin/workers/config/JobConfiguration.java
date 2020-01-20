package com.mityanin.workers.config;

import com.mityanin.workers.component.ColumnsEvenPartitioner;
import com.mityanin.workers.component.JobRegisterTasklet;
import com.mityanin.workers.component.JobUnRegisterTasklet;
import com.mityanin.workers.component.LoggingItemWriter;
import com.mityanin.workers.domain.entity.Work;
import com.mityanin.workers.domain.enums.Status;
import com.mityanin.workers.repository.WorkJobRepository;
import com.mityanin.workers.repository.WorkRepository;
import com.mityanin.workers.service.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
@Slf4j
public class JobConfiguration {

    @Value("${rows-per-fetch}")
    private Integer rowsPerFetch;

    @Value("${threads-quantity}")
    private Integer threadsQuantity;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final WorkRepository workRepository;

    private final WorkJobRepository jobRepository;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                            WorkRepository workRepository, WorkJobRepository jobRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.workRepository = workRepository;
        this.jobRepository = jobRepository;
    }

    @Bean
    public ColumnsEvenPartitioner partitioner() {
        return new ColumnsEvenPartitioner(workRepository, rowsPerFetch);
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Work> itemReader(
            @Value("#{stepExecutionContext['" + ColumnsEvenPartitioner.START + "']}") Long start,
            @Value("#{stepExecutionContext['" + ColumnsEvenPartitioner.STOP + "']}") Long stop
    ) throws Exception {
        log.debug("Processing range from {} to {}", start, stop);

        final RepositoryItemReader<Work> reader = new RepositoryItemReader<>();
        reader.setRepository(workRepository);
        reader.setMethodName("findAllByIdBetweenAndStatus");
        reader.setArguments(List.of(start, stop, Status.NEW));
        reader.setSort(Map.of("id", Sort.Direction.ASC));
        reader.setPageSize(rowsPerFetch);
        reader.setSaveState(false);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    @StepScope
    public LoggingItemWriter<Work> consoleItemWriter() {
        return new LoggingItemWriter<>();
    }

    @Bean
    @Qualifier("mainStep")
    public Step mainStep(@Qualifier("slaveStep") Step slaveStep, ColumnsEvenPartitioner partitioner) throws Exception {
        final ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(threadsQuantity);
        pool.setThreadNamePrefix("Slave-step-");
        pool.initialize();
        return stepBuilderFactory.get("step1")
                .partitioner(slaveStep.getName(), partitioner)
                .step(slaveStep)
                .gridSize(threadsQuantity)
                .taskExecutor(pool)
                .build();
    }

    @Bean
    public Function<Work, Work> getHttpStatusUpdater(RestService restService) {
        return work -> {
            try {
                work.setHttpCode(restService.getResponseCode(work.getUrl()));
                work.setStatus(Status.DONE);
            } catch (Exception e) {
                e.printStackTrace();
                work.setStatus(Status.ERROR);
            }
            return work;
        };
    }

    @Bean
    @Qualifier("slaveStep")
    public Step slaveStep(PlatformTransactionManager transactionManager,
                          RepositoryItemReader<Work> itemReader,
                          LoggingItemWriter<Work> itemWriter,
                          Function<Work, Work> httpStatusUpdater) throws Exception {
        return stepBuilderFactory.get("slaveStep")
                .transactionManager(transactionManager)
                .<Work, Work>chunk(rowsPerFetch)
                .reader(itemReader)
                .processor(httpStatusUpdater)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job job(@Qualifier("mainStep") Step mainStep) throws Exception {
        return jobBuilderFactory.get("job")
                .start(stepBuilderFactory.get("jobRegister")
                        .tasklet(new JobRegisterTasklet(jobRepository))
                        .build())
                .next(mainStep)
                .next(stepBuilderFactory.get("jobUnRegister")
                        .tasklet(new JobUnRegisterTasklet(jobRepository))
                        .build())
                .build();
    }
}
