package app.aws.sender;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Service
public class S3Sender implements ApplicationRunner {

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String bucketName = "elasticbeanstalk-us-east-1-459313905922";
        String key = "data/output.txt";
        String data = "TRADE_DATE 20200812 TIME 20:13:19 HHDDSS";

        log.info("Writing object to S3");

        InputStream iis = new ByteArrayInputStream(data.getBytes());
        amazonS3.putObject(bucketName, key, iis, new ObjectMetadata());

        log.info("Object is saved to S3");
    }
}
