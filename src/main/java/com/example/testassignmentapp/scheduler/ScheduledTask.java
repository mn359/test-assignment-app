package com.example.testassignmentapp.scheduler;

import com.example.testassignmentapp.exchangerate.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ScheduledTask {
    private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ScheduledTask(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }


    @Scheduled(cron = "${scheduled-task-first-cron}")
    public void executeFirstTime() {
        logger.info("Executing task first");
        executeTask();
    }

    @Scheduled(cron = "${scheduled-task-second-cron}")
    public void executeSecondTime() {
        logger.info("Executing task second");
        executeTask();
    }

    void executeTask() {
        exchangeRateService.updateExchangeRate();
    }
}
