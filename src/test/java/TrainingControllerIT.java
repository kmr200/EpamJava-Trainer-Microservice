import com.epam.xstack.gym.trainer.TrainerWorkloadServiceApplication;
import com.epam.xstack.gym.trainer.dto.request.training.ActionType;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.jpa.repository.TrainingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TrainerWorkloadServiceApplication.class)
@ActiveProfiles("test")
@EnableJpaRepositories(basePackages = "com.epam.xstack.gym.trainer.jpa.repository")
@AutoConfigureMockMvc
@Transactional
class TrainingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String trainerUsername = "trainer";
    private final String trainerFirstName = "John";
    private final String trainerLastName = "Doe";
    private final LocalDate trainingDate = LocalDate.now();
    private final Integer trainingDuration = 30;
    private final String createTrainingUri = "/api/v1/trainer-workload/training/";
    private final String contentType = "application/json";
    private final ModifyTrainingRequest create = new ModifyTrainingRequest(
            trainerUsername,
            trainerFirstName,
            trainerLastName,
            true,
            trainingDate,
            trainingDuration,
            ActionType.CREATE
    );
    private final ModifyTrainingRequest delete = new ModifyTrainingRequest(
            trainerUsername,
            trainerFirstName,
            trainerLastName,
            true,
            trainingDate,
            trainingDuration,
            ActionType.DELETE
    );

    @Test
    void whenCreateTraining_thenCreateTrainingAndTrainer() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post(createTrainingUri)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(create)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        TrainerEntity trainer = trainerRepository.findByUsernameIgnoreCase(trainerUsername)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer was not created"));
        List<TrainingEntity> training = trainingRepository.findAllTrainingsByTrainer(trainerUsername, LocalDate.now());

        //Check if only one training was created
        assertEquals(1, training.size());
        //Check if the correct trainer profile was created
        assertEquals(trainerUsername, trainer.getUsername());
        assertEquals(trainerFirstName, trainer.getFirstName());
        assertEquals(trainerLastName, trainer.getLastName());
    }

    @Test
    void givenTrainerAlreadyCreated_whenCreateTraining_thenKeepSingleTrainerProfile() throws Exception {

        //Save trainer before call
        trainerRepository.save(new TrainerEntity(
                trainerUsername,
                trainerFirstName,
                trainerLastName,
                true
        ));

        //If the request was successful -> trainer with same username was not created otherwise it would cause an exception since username is the key for table
        mockMvc.perform(MockMvcRequestBuilders
                        .post(createTrainingUri)
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        TrainerEntity trainer = trainerRepository.findByUsernameIgnoreCase(trainerUsername)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer was not created"));

        assertEquals(trainerUsername, trainer.getUsername());
        assertEquals(trainerFirstName, trainer.getFirstName());
        assertEquals(trainerLastName, trainer.getLastName());
    }

    @Test
    void givenEmptyRequiredField_whenCreateTraining_thenReturn4xx() throws Exception {

        ModifyTrainingRequest request1 = new ModifyTrainingRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/trainer-workload/")
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void givenNonExistingTraining_whenDeleteTraining_thenReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/trainer-workload/")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(delete)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenLegitimateTraining_whenDeleteTraining_thenDeleteTraining() throws Exception {
        //Create training
        mockMvc.perform(MockMvcRequestBuilders
                        .post(createTrainingUri)
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //Check if the training has indeed been created
        List<TrainingEntity> trainings = trainingRepository.findAllTrainingsByTrainer(trainerUsername, trainingDate);
        assertEquals(1, trainings.size());

        //Delete training
        mockMvc.perform(MockMvcRequestBuilders
                        .post(createTrainingUri)
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(delete)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        trainings = trainingRepository.findAllTrainingsByTrainer(trainerUsername, trainingDate);
        assertEquals(0, trainings.size());
    }

}
