package app.aws.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${accessKey}")
    private String accessKey;

    @Value("${securityKey}")
    private String securityKey;

    @Bean
    public AmazonS3 amazonS3() {

        AmazonS3 amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, securityKey));
        amazonS3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
        return amazonS3Client;
    }
}


