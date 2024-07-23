package com.chwipoClova.subscription.schedule;

import com.chwipoClova.recruit.service.RecruitService;
import com.chwipoClova.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionSchedule {
    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 30 8 * * WED")
    public void runTask() throws Exception {
        log.info("SubscriptionService Scheduled start");
        subscriptionService.sendSubscriptionEmail();
        log.info("SubscriptionService Scheduled end");
    }
}
