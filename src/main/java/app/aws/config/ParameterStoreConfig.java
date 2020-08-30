package app.aws.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterStoreConfig {

    @Value("${accessKey}")
    private String accessKey;

    @Value("${securityKey}")
    private String securityKey;

    @Bean
    public AWSSimpleSystemsManagement getSsm() {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, securityKey));
        AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClient.builder()
                .withCredentials(awsCredentialsProvider)
                .withRegion(Regions.US_EAST_1)
                .build();

        return ssm;
    }
}
