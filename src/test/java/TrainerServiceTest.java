import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.mapper.TrainerMapper;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    private static final String USERNAME = "test_username";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final Boolean STATUS_ACTIVE = true;
    private static final LocalDate TRAINING_DATE = LocalDate.of(2024, 12, 15);
    private static final Integer TRAINING_DURATION = 60;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void givenExistingTrainer_whenGetOrCreate_thenReturnTrainerDTO() {
        // given
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);

        // then
        verify(trainerRepository, times(1)).findByTrainerUsernameIgnoreCase(USERNAME);
        assertEquals(trainerDTO, result);
    }

    @Test
    void givenNonExistingTrainer_whenGetOrCreate_thenCreateAndReturnTrainerDTO() {
        // given
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());
        when(trainerRepository.save(any(TrainerEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(trainerMapper.toDTO(any(TrainerEntity.class))).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);

        // then
        verify(trainerRepository, times(1)).save(any(TrainerEntity.class));
        assertEquals(trainerDTO, result);
    }

    @Test
    void givenTrainerUsername_whenAddTraining_thenUpdateTrainingSummary() {
        // given
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        trainerEntity.setTrainingSummary(new TrainingSummary());
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.addTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        assertTrue(trainerEntity.getTrainingSummary().getSummary().get(TRAINING_DATE.getYear())
                .containsKey(TRAINING_DATE.getMonthValue()));
    }

    @Test
    void givenTrainerUsername_whenDeleteTraining_thenUpdateTrainingSummary() {
        // given
        TrainingSummary trainingSummary = new TrainingSummary();
        trainingSummary.addTraining(TRAINING_DATE, TRAINING_DURATION);

        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        trainerEntity.setTrainingSummary(trainingSummary);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        //Training summary should be deleted when it's empty
        assertTrue(trainerEntity.getTrainingSummary().getSummary().isEmpty());
    }

    @Test
    void givenMissingUsername_whenUpdateTrainer_thenThrowEmptyRequiredField() {
        // when / then
        assertThrows(EmptyRequiredField.class, () ->
                trainerService.updateTrainer(null, FIRST_NAME, LAST_NAME, STATUS_ACTIVE));
    }

    @Test
    void givenTrainerUsername_whenUpdateTrainer_thenUpdateFields() {
        // given
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, "OldFirstName", "OldLastName", false);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.updateTrainer(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        assertEquals(FIRST_NAME, trainerEntity.getFirstName());
        assertEquals(LAST_NAME, trainerEntity.getLastName());
        assertTrue(trainerEntity.getStatus());
    }

    @Test
    void givenExistingTraining_whenDeleteTraining_thenReduceTrainingDuration() {
        // given
        TrainingSummary trainingSummary = new TrainingSummary();
        trainingSummary.addTraining(TRAINING_DATE, TRAINING_DURATION + 30);

        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        trainerEntity.setTrainingSummary(trainingSummary);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        assertEquals(30, trainerEntity.getTrainingSummary().getSummary().get(TRAINING_DATE.getYear())
                .get(TRAINING_DATE.getMonthValue()));
    }

    @Test
    void givenExactTrainingDuration_whenDeleteTraining_thenRemoveMonthEntry() {
        // given
        TrainingSummary trainingSummary = new TrainingSummary();
        trainingSummary.addTraining(TRAINING_DATE, TRAINING_DURATION);

        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        trainerEntity.setTrainingSummary(trainingSummary);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        assertFalse(trainerEntity.getTrainingSummary().getSummary().containsKey(TRAINING_DATE.getYear()));
    }

    @Test
    void givenNonExistingTraining_whenDeleteTraining_thenNoChangesToTrainingSummary() {
        // given
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        trainerEntity.setTrainingSummary(new TrainingSummary());
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        assertTrue(trainerEntity.getTrainingSummary().getSummary().isEmpty());
    }

    @Test
    void givenEmptyTrainingSummary_whenDeleteTraining_thenNoChanges() {
        // given
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, STATUS_ACTIVE);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(trainerEntity)).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        // when
        TrainerDTO result = trainerService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION);

        // then
        verify(trainerRepository, times(1)).save(trainerEntity);
        assertEquals(trainerDTO, result);
        assertTrue(trainerEntity.getTrainingSummary().getSummary().isEmpty());
    }

    @Test
    void givenTrainerNotFound_whenDeleteTraining_thenThrowException() {
        // given
        when(trainerRepository.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());

        // when / then
        assertThrows(TrainerByUsernameNotFound.class, () ->
                trainerService.deleteTraining(USERNAME, TRAINING_DATE, TRAINING_DURATION));
        verify(trainerRepository, never()).save(any());
    }
}
