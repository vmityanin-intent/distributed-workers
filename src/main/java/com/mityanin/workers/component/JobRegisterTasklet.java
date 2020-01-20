package com.mityanin.workers.component;

import com.mityanin.workers.domain.entity.WorkJob;
import com.mityanin.workers.domain.enums.Status;
import com.mityanin.workers.repository.WorkJobRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@AllArgsConstructor
public class JobRegisterTasklet implements Tasklet {

    public static final String WORK_JOB_ID = "workJobId";
    private WorkJobRepository workJobRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final WorkJob wj = workJobRepository.save(WorkJob.builder().status(Status.PROCESSING).build());
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .putLong(WORK_JOB_ID, wj.getId());

        return RepeatStatus.FINISHED;
    }
}
