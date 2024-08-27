package com.example.notificationservice.scheduler;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Implement job logic here
    }
}

