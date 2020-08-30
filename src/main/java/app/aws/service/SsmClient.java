package app.aws.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SsmClient implements CommandLineRunner {
    @Autowired
    private AWSSimpleSystemsManagement ssm;

    @Override
    public void run(String... args) throws Exception {
        GetParameterRequest getUserNameParamRequest = new GetParameterRequest().withName("USERNAME");

        GetParameterResult userNameResult = ssm.getParameter(getUserNameParamRequest);

        String userName = userNameResult.getParameter().getName();
        String userNameVal = userNameResult.getParameter().getValue();

        log.info("username {} and userNameVal {} from parameter store ", userName, userNameVal);

        GetParameterRequest getPasswordParamRequest = new GetParameterRequest().withName("PASSWORD");

        GetParameterResult passwordResult = ssm.getParameter(getPasswordParamRequest);

        String passwordKey = passwordResult.getParameter().getName();
        String passwordVal = passwordResult.getParameter().getValue();

        log.info("passwordKey {} and passwordVal {} from parameter store ", passwordKey, passwordVal);
    }
}
