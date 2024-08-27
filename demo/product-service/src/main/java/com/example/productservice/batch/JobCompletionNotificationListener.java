package com.example.productservice.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Logic to execute before the job starts
        System.out.println("Job Starting: " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // Logic to execute after the job completes
        if (jobExecution.getStatus().isUnsuccessful()) {
            System.out.println("Job Failed: " + jobExecution.getJobInstance().getJobName());
        } else {
            System.out.println("Job Completed: " + jobExecution.getJobInstance().getJobName());
        }
    }
}
