package steps.summary;

import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = SummaryCucumberConfiguration.class)
public class SummarySteps {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrainerRepository trainerRepository;

    private static final String BASE_URL = "/api/v1/trainer-workload/trainer";

    private ResponseEntity<String> response;

    private static final LocalDate today = LocalDate.now();
    private static final Integer trainingDuration = 30;
    private static final String USERNAME = "michael.wilson";
    private static final String FIRSTNAME = "Michael";
    private static final String LASTNAME = "Wilson";
    private static final TrainerEntity trainerEntity = new TrainerEntity(
            USERNAME,
            FIRSTNAME,
            LASTNAME,
            true);

    @Given("trainer profile")
    public void trainerProfile() {
        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME))
                .thenReturn(Optional.of(trainerEntity));
    }

    @Given("trainer profile with trainings registered")
    public void trainerProfileWithTrainings() {
        trainerEntity.setTrainingSummary(new TrainingSummary().addTraining(today, trainingDuration));
        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME))
                .thenReturn(Optional.of(trainerEntity));
    }

    @Given("non-existing trainer profile")
    public void nonExistingTrainerProfile() {
        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME))
                .thenReturn(Optional.empty());
    }

    @When("training summary requested")
    public void trainingSummaryRequested() {
        // Make the actual request after mocking the repository
        response = restTemplate.getForEntity(BASE_URL + "/" + USERNAME + "/trainings", String.class);
    }

    @Then("return summary")
    public void returnSummary() {
        // Verify the response and content
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("trainings"));
    }

    @Then("return non-empty summary")
    public void returnNonEmptySummary() throws JsonProcessingException {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                trainingDuration.toString(),
                objectMapper.
                        readTree(response.getBody())
                        .get("trainings")
                        .get("" + today.getYear())
                        .get("" + today.getMonthValue()).toString()
        );
    }

    @Then("return not-found")
    public void returnNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
