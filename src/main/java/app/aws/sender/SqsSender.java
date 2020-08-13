package app.aws.sender;

import app.aws.model.Trade;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class SqsSender implements ApplicationRunner {

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private Session session;

    private Gson gson = new Gson();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 2; i++) {
            String gsonMsg = getGsonMsg(i);
            sendMessage(gsonMsg);
        }
    }

    public String getGsonMsg(int i) {
        return gson.toJson(new Trade(UUID.randomUUID().toString(),
                new Random().nextInt(i+1),
                0.9 * new Random().nextFloat()));
    }

    public void sendMessage(String message) {
        TextMessage textMessage = null;
        try {
            textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);
            log.info("message sent :" + message);
        } catch (JMSException e) {
            log.error("error while sending message ", e);
        }
    }
}
