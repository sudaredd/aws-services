package app.aws;

import app.aws.config.ParameterStoreConfig;
import app.aws.config.S3Config;
import app.aws.config.SqsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@Import({SqsConfig.class, S3Config.class, ParameterStoreConfig.class})
public class AwsSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(AwsSampleApplication.class, args);
    }
}
