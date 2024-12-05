import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
import com.epam.xstack.gym.trainer.dto.request.training.ActionType;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import com.epam.xstack.gym.trainer.service.MessageConsumer;
import com.epam.xstack.gym.trainer.service.TrainerService;
import com.epam.xstack.gym.trainer.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageConsumerTest {

    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final boolean ACTIVE = true;
    private static final LocalDate TRAINING_DATE = LocalDate.of(2023, 12, 4);
    private static final int TRAINING_DURATION = 60;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private MessageConsumer messageConsumer;

    @Test
    void givenCreateActionType_whenReceiveModifyTrainingMessage_thenCreateTraining() {
        ModifyTrainingRequest request = new ModifyTrainingRequest(
                USERNAME,
                FIRST_NAME,
                LAST_NAME,
                ACTIVE,
                TRAINING_DATE,
                TRAINING_DURATION,
                ActionType.CREATE
        );

        TrainerDTO trainerDTO = new TrainerDTO();
        when(trainerService.getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, ACTIVE))
                .thenReturn(trainerDTO);

        TrainingDTO trainingDTO = new TrainingDTO();
        when(trainingService.addTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION))
                .thenReturn(trainingDTO);

        // Act
        messageConsumer.receiveModifyTrainingMessage(request);

        // Assert
        verify(trainerService, times(1)).getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, ACTIVE);
        verify(trainingService, times(1)).addTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);
    }

    @Test
    void givenDeleteActionType_whenReceiveModifyTrainingMessage_thenDeleteTraining() {
        // Arrange
        ModifyTrainingRequest request = new ModifyTrainingRequest(
                USERNAME,
                FIRST_NAME,
                LAST_NAME,
                ACTIVE,
                TRAINING_DATE,
                TRAINING_DURATION,
                ActionType.DELETE
        );

        TrainingDTO trainingDTO = new TrainingDTO();
        when(trainingService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION))
                .thenReturn(trainingDTO);

        // Act
        messageConsumer.receiveModifyTrainingMessage(request);

        // Assert
        verify(trainingService, times(1)).deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);
    }

    @Test
    void givenInvalidActionType_whenReceiveModifyTrainingMessage_thenThrowException() {
        // Arrange
        ModifyTrainingRequest request = new ModifyTrainingRequest(
                USERNAME,
                FIRST_NAME,
                LAST_NAME,
                ACTIVE,
                TRAINING_DATE,
                TRAINING_DURATION,
                null
        );

        // Act & Assert
        assertThrows(EmptyRequiredField.class, () -> messageConsumer.receiveModifyTrainingMessage(request));
    }

    @Test
    void givenValidRequest_whenReceiveUpdateTrainerMessage_thenUpdateTrainer() {
        // Arrange
        UpdateTrainerRequest request = new UpdateTrainerRequest();
        request.setUsername(USERNAME);
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setActive(ACTIVE);

        TrainerDTO trainerDTO = new TrainerDTO();
        when(trainerService.updateTrainer(USERNAME, FIRST_NAME, LAST_NAME, ACTIVE))
                .thenReturn(trainerDTO);

        // Act
        messageConsumer.receiveUpdateTrainerMessage(request);

        // Assert
        verify(trainerService, times(1))
                .updateTrainer(USERNAME, FIRST_NAME, LAST_NAME, ACTIVE);
    }
}
