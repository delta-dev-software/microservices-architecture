package com.example.notificationservice.infrastructure.config;

/*
import com.example.notificationservice.task.NotificationJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail notificationJobDetail() {
        return JobBuilder.newJob(NotificationJob.class)
                .withIdentity("notificationJob")
                .usingJobData("notificationId", "sample-id") // Sample data; adjust as needed
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger notificationJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(notificationJobDetail())
                .withIdentity("notificationJobTrigger")
                .withSchedule(org.quartz.CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")) // Every minute
                .build();
    }
}*/


