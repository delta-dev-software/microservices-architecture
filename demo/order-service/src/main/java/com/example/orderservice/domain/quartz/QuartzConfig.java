package com.example.orderservice.domain.quartz;


import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail processOrderJobDetail() {
        return JobBuilder.newJob(ProcessOrderJob.class)
                .withIdentity("processOrderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger processOrderTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)  // Adjust the interval to your needs
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(processOrderJobDetail())
                .withIdentity("processOrderTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
