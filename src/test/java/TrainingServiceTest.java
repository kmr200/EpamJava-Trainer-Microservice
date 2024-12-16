import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    private static final String TRAINER_USERNAME = "trainerUsername";
    private static final LocalDate TRAINING_DATE = LocalDate.of(2024, 1, 15);
    private static final Integer TRAINING_DURATION = 60;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void givenValidTrainer_whenAddTraining_thenReturnTrainingDTO() throws TrainerByUsernameNotFound {
        TrainerEntity trainerEntity = new TrainerEntity(TRAINER_USERNAME, "John", "Doe", true);
        TrainingEntity trainingEntity = new TrainingEntity(TRAINING_DATE, TRAINING_DURATION, trainerEntity);
        TrainingDTO trainingDTO = new TrainingDTO(new TrainerDTO(
                TRAINER_USERNAME,
                "John",
                "Doe",
                true
        ),TRAINING_DATE, TRAINING_DURATION, UUID.randomUUID().toString());

        when(trainerRepository.findByUsernameIgnoreCase(TRAINER_USERNAME)).thenReturn(Optional.of(trainerEntity));
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(trainingEntity);
        when(trainingMapper.toDto(trainingEntity)).thenReturn(trainingDTO);

        TrainingDTO result = trainingService.addTraining(TRAINER_USERNAME, TRAINING_DATE, TRAINING_DURATION);

        assertEquals(trainingDTO, result);
        verify(trainerRepository).findByUsernameIgnoreCase(TRAINER_USERNAME);
        verify(trainingRepository).save(any(TrainingEntity.class));
        verify(trainingMapper).toDto(trainingEntity);
    }

    @Test
    void givenInvalidTrainer_whenAddTraining_thenThrowTrainerByUsernameNotFound() {
        when(trainerRepository.findByUsernameIgnoreCase(TRAINER_USERNAME)).thenReturn(Optional.empty());

        TrainerByUsernameNotFound exception = assertThrows(
                TrainerByUsernameNotFound.class,
                () -> trainingService.addTraining(TRAINER_USERNAME, TRAINING_DATE, TRAINING_DURATION)
        );

        assertEquals("Could not create training. Trainer with username " + TRAINER_USERNAME + " not found.", exception.getMessage());
        verify(trainerRepository).findByUsernameIgnoreCase(TRAINER_USERNAME);
        verifyNoInteractions(trainingRepository);
    }

    @Test
    void givenTrainerWithTrainings_whenGetSortedTrainingsByTrainer_thenReturnGroupedTrainings() {
        TrainerEntity trainerEntity = new TrainerEntity(TRAINER_USERNAME, "John", "Doe", true);
        List<TrainingEntity> trainings = List.of(
                new TrainingEntity(LocalDate.of(2024, 1, 10), 60, trainerEntity),
                new TrainingEntity(LocalDate.of(2024, 1, 20), 90, trainerEntity),
                new TrainingEntity(LocalDate.of(2024, 2, 15), 45, trainerEntity)
        );

        when(trainingRepository.findAllTrainingsByTrainer(TRAINER_USERNAME, LocalDate.now())).thenReturn(trainings);

        Map<Integer, Map<Integer, Integer>> result = trainingService.getSortedTrainingsByTrainer(TRAINER_USERNAME);

        Map<Integer, Map<Integer, Integer>> expected = new HashMap<>();
        expected.put(2024, Map.of(
                1, 150, // January: 60 + 90
                2, 45   // February: 45
        ));

        assertEquals(expected, result);
        verify(trainingRepository).findAllTrainingsByTrainer(TRAINER_USERNAME, LocalDate.now());
    }

    @Test
    void givenNoTrainings_whenGetSortedTrainingsByTrainer_thenReturnEmptyMap() {
        when(trainingRepository.findAllTrainingsByTrainer(TRAINER_USERNAME, LocalDate.now())).thenReturn(Collections.emptyList());

        Map<Integer, Map<Integer, Integer>> result = trainingService.getSortedTrainingsByTrainer(TRAINER_USERNAME);

        assertTrue(result.isEmpty());
        verify(trainingRepository).findAllTrainingsByTrainer(TRAINER_USERNAME, LocalDate.now());
    }
}
