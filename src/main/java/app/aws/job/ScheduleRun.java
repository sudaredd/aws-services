package app.aws.job;

import app.aws.sender.SqsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ScheduleRun {

    @Autowired
    private SqsSender sqsSender;

    private AtomicInteger counter = new AtomicInteger();

    @Scheduled(cron = "*/5 8-22 * * * ?")
    public void runMins() {

        String message = sqsSender.getGsonMsg(counter.getAndIncrement());

        sqsSender.sendMessage(message);

        log.info("sent message from cron : " + message);
    }
}
