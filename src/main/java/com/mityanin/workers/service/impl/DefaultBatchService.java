package com.mityanin.workers.service.impl;

import com.mityanin.workers.repository.WorkRepository;
import com.mityanin.workers.service.BatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

@AllArgsConstructor
@Slf4j
public class DefaultBatchService implements BatchService {

    private JobLauncher jobLauncher;

    private Job job;

    @Override
    @Async
    public void run() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobId", String.valueOf(System.currentTimeMillis()))
                .addDate("date", new Date())
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);

        log.info("Job finished with status: {}", execution.getStatus());


    }
}
