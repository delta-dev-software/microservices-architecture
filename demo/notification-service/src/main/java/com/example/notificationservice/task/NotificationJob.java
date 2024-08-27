package com.example.notificationservice.task;

/*
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class NotificationJob implements Job {

    private final NotificationTaskExecutor notificationTaskExecutor;

    public NotificationJob(NotificationTaskExecutor notificationTaskExecutor) {
        this.notificationTaskExecutor = notificationTaskExecutor;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String notificationId = context.getJobDetail().getJobDataMap().getString("notificationId");

        // Ensure notificationId is not null
        if (notificationId != null) {
            notificationTaskExecutor.executeTask(notificationId);
        } else {
            throw new JobExecutionException("Notification ID is missing in JobDataMap");
        }
    }
}
*/


