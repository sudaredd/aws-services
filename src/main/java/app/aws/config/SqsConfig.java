package app.aws.config;

import app.aws.listener.SqsListener;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.*;

@Configuration
public class SqsConfig {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${securityKey}")
    private String securityKey;

    @Value("${standardQueueUrl}")
    private String standardQueueUrl;

    @Value("${queue}")
    private String queue;

    @Bean
    public ConnectionFactory connectionFactory() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, securityKey);

        AmazonSQSClientBuilder standard = AmazonSQSClientBuilder.standard();
        standard.setCredentials(new AWSStaticCredentialsProvider(credentials));
        standard.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(standardQueueUrl, Region.getRegion(Regions.US_EAST_1).getName()));
        return new SQSConnectionFactory(new ProviderConfiguration(), standard);

 /*       SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder()
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Region.getRegion(Regions.US_EAST_1))
                .withEndpoint(standardQueueUrl)
                .build();
        return connectionFactory;*/
    }

    @Bean
    public Connection connection(ConnectionFactory connectionFactory) throws JMSException {
        return connectionFactory.createConnection();
    }

    @Bean
    public Session session(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public MessageProducer messageProducer(Session session) throws JMSException {
        MessageProducer producer = session.createProducer(session.createQueue(queue));
        return producer;
    }

    @Bean
    public MessageConsumer messageConsumer(Connection connection, Session session) throws JMSException {
        MessageConsumer consumer = session.createConsumer(session.createQueue(queue));
        consumer.setMessageListener(new SqsListener());
        connection.start();
        return consumer;
    }
}
