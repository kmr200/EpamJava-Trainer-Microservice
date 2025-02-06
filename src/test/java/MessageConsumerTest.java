import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
import com.epam.xstack.gym.trainer.dto.request.training.ActionType;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import com.epam.xstack.gym.trainer.service.MessageConsumer;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    private static final String USERNAME = "trainer123";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final boolean STATUS = true;
    private static final int TRAINING_DURATION = 60;
    private static final LocalDate TRAINING_DATE = LocalDate.now();

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private MessageConsumer messageConsumer;

    private ModifyTrainingRequest modifyTrainingRequest;
    private UpdateTrainerRequest updateTrainerRequest;

    @BeforeEach
    void setup() {
        modifyTrainingRequest = new ModifyTrainingRequest();
        modifyTrainingRequest.setUsername(USERNAME);
        modifyTrainingRequest.setFirstName(FIRST_NAME);
        modifyTrainingRequest.setLastName(LAST_NAME);
        modifyTrainingRequest.setStatus(STATUS);
        modifyTrainingRequest.setTrainingDuration(TRAINING_DURATION);
        modifyTrainingRequest.setTrainingDate(TRAINING_DATE);

        updateTrainerRequest = new UpdateTrainerRequest();
        updateTrainerRequest.setUsername(USERNAME);
        updateTrainerRequest.setFirstName(FIRST_NAME);
        updateTrainerRequest.setLastName(LAST_NAME);
        updateTrainerRequest.setActive(STATUS);
    }

    @Test
    void receiveModifyTrainingMessage_WhenCreateAction_ShouldCallAddTraining() {
        // Given
        modifyTrainingRequest.setActionType(ActionType.CREATE);

        when(trainerService.getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, STATUS))
                .thenReturn(new TrainerDTO());

        // When
        messageConsumer.receiveModifyTrainingMessage(modifyTrainingRequest);

        // Then
        verify(trainerService).getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, STATUS);
        verify(trainerService).addTraining(USERNAME, modifyTrainingRequest.getTrainingDate(), TRAINING_DURATION);
    }

    @Test
    void receiveModifyTrainingMessage_WhenDeleteAction_ShouldCallDeleteTraining() {
        // Given
        modifyTrainingRequest.setActionType(ActionType.DELETE);

        // When
        messageConsumer.receiveModifyTrainingMessage(modifyTrainingRequest);

        // Then
        verify(trainerService).deleteTraining(USERNAME, modifyTrainingRequest.getTrainingDate(), TRAINING_DURATION);
    }

    @Test
    void receiveModifyTrainingMessage_WhenActionTypeIsNull_ShouldThrowException() {
        // Given
        modifyTrainingRequest.setActionType(null);

        // When/Then
        assertThrows(EmptyRequiredField.class, () -> messageConsumer.receiveModifyTrainingMessage(modifyTrainingRequest));
    }

    @Test
    void receiveUpdateTrainerMessage_ShouldCallUpdateTrainer() {
        // When
        messageConsumer.receiveUpdateTrainerMessage(updateTrainerRequest);

        // Then
        verify(trainerService).updateTrainer(USERNAME, FIRST_NAME, LAST_NAME, STATUS);
    }

}
