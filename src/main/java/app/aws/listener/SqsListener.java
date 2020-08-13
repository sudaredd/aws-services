package app.aws.listener;

import app.aws.model.Trade;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Slf4j
public class SqsListener implements MessageListener {
    private Gson gson = new Gson();

    @Override
    public void onMessage(javax.jms.Message message) {
        try {
            log.info("Message received:" + ((TextMessage) message).getText());
            String text = ((TextMessage) message).getText();
            Trade trade = gson.fromJson(text, Trade.class);
            log.info("tradeId:" + trade.getTradeId());
        } catch (JMSException e) {
            log.error("error occured while reading ", e);
        }
    }
}
