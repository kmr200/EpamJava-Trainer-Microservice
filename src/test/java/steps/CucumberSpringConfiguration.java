package steps;

import com.epam.xstack.gym.trainer.TrainerWorkloadServiceApplication;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.service.MessageConsumer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(
        classes = TrainerWorkloadServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration-test")
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class CucumberSpringConfiguration {

    @MockBean
    private TrainerRepository trainerRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessageConsumer messageConsumer;

}
