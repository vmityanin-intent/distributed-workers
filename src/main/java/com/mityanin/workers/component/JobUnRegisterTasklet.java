package com.mityanin.workers.component;

import com.mityanin.workers.repository.WorkJobRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import static com.mityanin.workers.component.JobRegisterTasklet.WORK_JOB_ID;

@AllArgsConstructor
public class JobUnRegisterTasklet implements Tasklet {

    private WorkJobRepository workJobRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final ExecutionContext executionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();
        workJobRepository.deleteById(executionContext.getLong(WORK_JOB_ID));
        return RepeatStatus.FINISHED;
    }
}
