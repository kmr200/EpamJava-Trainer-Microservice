import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.jpa.repository.TrainingRepository;
import com.epam.xstack.gym.trainer.mapper.TrainerMapper;
import com.epam.xstack.gym.trainer.mapper.TrainingMapper;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void testGetOrCreateWhenTrainerExists() {
        String username = "existingTrainer";
        TrainerEntity trainerEntity = new TrainerEntity(username, "John", "Doe", true);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(trainerEntity));
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        TrainerDTO result = trainerService.getOrCreate(username, "John", "Doe", true);

        assertEquals(trainerDTO, result);
        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(username);
        verify(trainerMapper, times(1)).toDTO(trainerEntity);
    }

    @Test
    void testGetOrCreateWhenTrainerDoesNotExist() {
        String username = "newTrainer";
        TrainerEntity trainerEntity = new TrainerEntity(username, "Jane", "Smith", true);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.empty());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(trainerEntity);
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        TrainerDTO result = trainerService.getOrCreate(username, "Jane", "Smith", true);

        assertEquals(trainerDTO, result);
        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(username);
        verify(trainerRepository, times(1)).save(any(TrainerEntity.class));
        verify(trainerMapper, times(1)).toDTO(trainerEntity);
    }

    @Test
    void testAddTrainingWhenTrainerExists() throws TrainerByUsernameNotFound {
        String username = "trainer";
        LocalDate date = LocalDate.now();
        int duration = 60;
        TrainerEntity trainerEntity = new TrainerEntity(username, "John", "Doe", true);
        TrainingEntity trainingEntity = new TrainingEntity(date, duration, trainerEntity);
        TrainingDTO trainingDTO = new TrainingDTO();

        when(trainerRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(trainerEntity));
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(trainingEntity);
        when(trainingMapper.toDto(trainingEntity)).thenReturn(trainingDTO);

        TrainingDTO result = trainerService.addTraining(username, date, duration);

        assertEquals(trainingDTO, result);
        verify(trainingRepository, times(1)).save(any(TrainingEntity.class));
        verify(trainingMapper, times(1)).toDto(trainingEntity);
    }

    @Test
    void testAddTrainingWhenTrainerDoesNotExist() {
        String username = "nonExistentTrainer";
        LocalDate date = LocalDate.now();
        int duration = 90;

        when(trainerRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.empty());

        assertThrows(TrainerByUsernameNotFound.class, () -> trainerService.addTraining(username, date, duration));
        verify(trainingRepository, never()).save(any(TrainingEntity.class));
    }

    @Test
    void testGetTrainerByUsername() {
        String username = "trainerUsername";
        TrainerEntity trainerEntity = new TrainerEntity(username, "John", "Doe", true);
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(trainerEntity));
        when(trainerMapper.toDTO(trainerEntity)).thenReturn(trainerDTO);

        TrainerDTO result = trainerService.getTrainerByUsername(username);

        assertEquals(trainerDTO, result);
        verify(trainerRepository, times(1)).findByUsernameIgnoreCase(username);
        verify(trainerMapper, times(1)).toDTO(trainerEntity);
    }

    @Test
    void testGetSortedTrainingsByTrainer() {
        String username = "trainer";
        TrainingEntity training1 = new TrainingEntity(LocalDate.of(2023, 5, 1), 60, null);
        TrainingEntity training2 = new TrainingEntity(LocalDate.of(2023, 5, 15), 90, null);
        TrainingEntity training3 = new TrainingEntity(LocalDate.of(2023, 6, 10), 45, null);

        when(trainingRepository.findAllTrainingsByTrainerSortedByYearAndMonth(username)).thenReturn(List.of(training1, training2, training3));

        Map<Integer, Map<Integer, Integer>> result = trainerService.getSortedTrainingsByTrainer(username);

        assertEquals(2, result.get(2023).size());
        assertEquals(150, result.get(2023).get(5)); // May total
        assertEquals(45, result.get(2023).get(6));  // June total
        verify(trainingRepository, times(1)).findAllTrainingsByTrainerSortedByYearAndMonth(username);
    }
}
