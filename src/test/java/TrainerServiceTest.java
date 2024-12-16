import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.mapper.TrainerMapper;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    private static final String USERNAME = "testUsername";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final Boolean IS_ACTIVE = true;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void givenExistingTrainer_whenGetOrCreate_thenReturnTrainerDTO() {
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);
        TrainerDTO trainerDTO = new TrainerDTO(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);

        when(trainerRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        TrainerDTO result = trainerService.getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);

        assertEquals(trainerDTO, result);
        verify(trainerRepository).findByUsernameIgnoreCase(USERNAME);
        verify(trainerMapper).toDTO(trainerEntity);
    }

    @Test
    void givenNonExistingTrainer_whenGetOrCreate_thenCreateAndReturnTrainerDTO() {
        TrainerEntity newTrainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);
        TrainerDTO trainerDTO = new TrainerDTO(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);

        when(trainerRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(newTrainerEntity);
        when(trainerMapper.toDTO(newTrainerEntity)).thenReturn(trainerDTO);

        TrainerDTO result = trainerService.getOrCreate(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);

        assertEquals(trainerDTO, result);
        verify(trainerRepository).findByUsernameIgnoreCase(USERNAME);
        verify(trainerRepository).save(any(TrainerEntity.class));
        verify(trainerMapper).toDTO(newTrainerEntity);
    }

    @Test
    void givenTrainerUsername_whenGetTrainerByUsername_thenReturnTrainerDTO() {
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);
        TrainerDTO trainerDTO = new TrainerDTO(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);

        when(trainerRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        TrainerDTO result = trainerService.getTrainerByUsername(USERNAME);

        assertEquals(trainerDTO, result);
        verify(trainerRepository).findByUsernameIgnoreCase(USERNAME);
        verify(trainerMapper).toDTO(trainerEntity);
    }

    @Test
    void givenNonExistingTrainerUsername_whenGetTrainerByUsername_thenThrowException() {
        when(trainerRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());

        assertThrows(TrainerByUsernameNotFound.class, () -> trainerService.getTrainerByUsername(USERNAME));

        verify(trainerRepository).findByUsernameIgnoreCase(USERNAME);
    }

    @Test
    void givenUpdatedFields_whenUpdateTrainer_thenReturnUpdatedTrainerDTO() {
        TrainerEntity trainerEntity = new TrainerEntity(USERNAME, FIRST_NAME, LAST_NAME, IS_ACTIVE);
        TrainerDTO updatedTrainerDTO = new TrainerDTO(USERNAME, "UpdatedFirstName", "UpdatedLastName", false);

        when(trainerRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(updatedTrainerDTO);

        TrainerDTO result = trainerService.updateTrainer(USERNAME, "UpdatedFirstName", "UpdatedLastName", false);

        assertEquals(updatedTrainerDTO, result);
        verify(trainingRepository).deleteAllByTrainer(USERNAME);
        verify(trainerRepository).save(trainerEntity);
        verify(trainerMapper).toDTO(trainerEntity);
    }

    @Test
    void givenNullUsername_whenUpdateTrainer_thenThrowException() {
        assertThrows(EmptyRequiredField.class, () -> trainerService.updateTrainer(null, FIRST_NAME, LAST_NAME, IS_ACTIVE));
    }
}
