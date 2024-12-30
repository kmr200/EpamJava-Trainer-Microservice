package steps;

import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
import com.epam.xstack.gym.trainer.dto.request.training.ActionType;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.service.MessageConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = CucumberSpringConfiguration.class)
public class ModifyTrainingSteps {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MessageConsumer messageConsumer;

    private static final LocalDate today = LocalDate.now();
    private static final Integer trainingDuration = 30;
    private static final String USERNAME = "michael.wilson";
    private static final String FIRSTNAME = "Michael";
    private static final String LASTNAME = "Wilson";
    private static Exception caughtException;
    private static final TrainerEntity trainerEntity = new TrainerEntity(
            USERNAME,
            FIRSTNAME,
            LASTNAME,
            true);

    // GIVEN

    @Given("trainer profile with initial trainings")
    public void trainerProfileWithInitialTrainings() {
        trainerEntity.setTrainingSummary(new TrainingSummary().addTraining(today, trainingDuration));
        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME))
                .thenReturn(Optional.of(trainerEntity));
    }

    @Given("non-existing trainer details")
    public void nonExistingTrainerDetails() {
        trainerEntity.setTrainingSummary(
                new TrainingSummary()
        );
        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(trainerEntity));
    }

    // WHEN

    @When("create training request comes")
    public void createTrainingRequestComes() {
        // Simulate the request
        messageConsumer.receiveModifyTrainingMessage(
                new ModifyTrainingRequest(
                        USERNAME,
                        FIRSTNAME,
                        LASTNAME,
                        true,
                        today,
                        trainingDuration,
                        ActionType.CREATE
                )
        );
    }

    @When("delete training request comes")
    public void deleteTrainingRequestComes() {
        // Subtract half of the training duration
        try {
            messageConsumer.receiveModifyTrainingMessage(
                    new ModifyTrainingRequest(
                            USERNAME,
                            FIRSTNAME,
                            LASTNAME,
                            true,
                            today,
                            trainingDuration / 2,
                            ActionType.DELETE
                    )
            );
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @When("update trainer request comes")
    public void updateTrainingRequestComes() {
        try {
            messageConsumer.receiveUpdateTrainerMessage(
                    new UpdateTrainerRequest(
                            USERNAME,
                            "test",
                            "test",
                            false
                    )
            );
        } catch (Exception e) {
            caughtException = e;
        }
    }

    // THEN

    @Then("update trainer's trainings summary")
    public void updateTrainingSummary() {
        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository).save(captor.capture());

        TrainerEntity trainerEntity = captor.getValue();
        TrainingSummary trainingSummary = trainerEntity.getTrainingSummary();
        assertEquals(
                String.valueOf(trainingDuration * 2),
                trainingSummary.getSummary()
                        .get(today.getYear())
                        .get(today.getMonthValue()).toString()
        );
    }

    @Then("create new trainer profile")
    public void createNewTrainerProfile() {
        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository, times(2)).save(captor.capture());

        //Get the argument of the second trainerRepository.save call as it contains summary as well
        TrainerEntity trainerEntity = captor.getAllValues().get(1);
        assertNotNull(trainerEntity);
        assertEquals(USERNAME, trainerEntity.getTrainerUsername());
        assertEquals(FIRSTNAME, trainerEntity.getFirstName());
        assertEquals(LASTNAME, trainerEntity.getLastName());
        assertEquals(
                String.valueOf(trainingDuration),
                trainerEntity.getTrainingSummary().getSummary()
                        .get(today.getYear())
                        .get(today.getMonthValue()).toString()
        );
    }

    @Then("Subtract training duration from trainer's trainings summary")
    public void subtractTrainingDuration() {
        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository).save(captor.capture());

        TrainerEntity trainerEntity = captor.getValue();
        assertNotNull(trainerEntity);
        TrainingSummary trainingSummary = trainerEntity.getTrainingSummary();
        assertEquals(
                String.valueOf(trainingDuration / 2),
                trainingSummary.getSummary()
                        .get(today.getYear())
                        .get(today.getMonthValue()).toString()
        );
    }

    @Then("return trainer not-found")
    public void returnTrainerNotFound() {
        assertNotNull(caughtException);
        assertTrue(caughtException instanceof TrainerByUsernameNotFound);
    }

    @Then("update trainer details")
    public void updateTrainerDetails() {
        ArgumentCaptor<TrainerEntity> captor = ArgumentCaptor.forClass(TrainerEntity.class);
        verify(trainerRepository).save(captor.capture());

        TrainerEntity trainerEntity = captor.getValue();
        assertNotNull(trainerEntity);
        assertEquals(USERNAME, trainerEntity.getTrainerUsername());
        assertEquals("test", trainerEntity.getFirstName());
        assertEquals("test", trainerEntity.getLastName());
        assertEquals(false, trainerEntity.getStatus());
    }
}
