//package com.rashmita.accuralsservice.scheduler;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import jakarta.annotation.PostConstruct;
//import java.time.ZonedDateTime;
//import java.time.ZoneId;
//
//@Slf4j
//@Component
//public class DailyAccrualsTest {
//
//    @PostConstruct
//    public void init() {
//        log.info(" DailyAccrualsTest initialized. Kathmandu time: {}",
//                ZonedDateTime.now(ZoneId.of("Asia/Kathmandu")));
//    }
//
//    @Scheduled(cron = "0 */1 * * * ?", zone = "Asia/Kathmandu")
//    public void runTest() {
//        log.info("⏰ Scheduler triggered at Kathmandu time: {}",
//                ZonedDateTime.now(ZoneId.of("Asia/Kathmandu")));
//    }
//}
