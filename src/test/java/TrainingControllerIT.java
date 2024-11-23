import com.epam.xstack.gym.trainer.TrainerWorkloadServiceApplication;
import com.epam.xstack.gym.trainer.dto.request.training.CreateTrainingRequest;
import com.epam.xstack.gym.trainer.exception.TrainerByUsernameNotFound;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.jpa.repository.TrainingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String trainerUsername = "trainer";
    private final String trainerFirstName = "John";
    private final String trainerLastName = "Doe";
    private final LocalDate trainingDate = LocalDate.now();
    private final Integer trainingDuration = 30;
    private final CreateTrainingRequest request = new CreateTrainingRequest(
            trainerUsername,
            trainerFirstName,
            trainerLastName,
            true,
            trainingDate,
            trainingDuration
    );

    @Test
    void whenCreateTraining_thenCreateTrainingAndTrainer() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/trainer-workload/")
                .contentType("application/JSON")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        TrainerEntity trainer = trainerRepository.findByUsernameIgnoreCase(trainerUsername)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer was not created"));
        List<TrainingEntity> training = trainingRepository.findAllTrainingsByTrainerSortedByYearAndMonth(trainerUsername);

        //Check if only one training was created
        assertEquals(1, training.size());
        //Check if the correct trainer profile was created
        assertEquals(trainerUsername, trainer.getUsername());
        assertEquals(trainerFirstName, trainer.getFirstName());
        assertEquals(trainerLastName, trainer.getLastName());
    }

    @Test
    void givenTrainerAlreadyCreated_whenCreateTraining_thenKeepSingleTrainerProfile() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());

        //Save trainer before call
        trainerRepository.save(new TrainerEntity(
                trainerUsername,
                trainerFirstName,
                trainerLastName,
                true
        ));

        //If the request was successful -> trainer with same username was not created otherwise it would cause an exception since username is the key for table
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/trainer-workload/")
                        .contentType("application/JSON")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        TrainerEntity trainer = trainerRepository.findByUsernameIgnoreCase(trainerUsername)
                .orElseThrow(() -> new TrainerByUsernameNotFound("Trainer was not created"));

        assertEquals(trainerUsername, trainer.getUsername());
        assertEquals(trainerFirstName, trainer.getFirstName());
        assertEquals(trainerLastName, trainer.getLastName());
    }

    @Test
    void whenGetTrainings_thenReturnTrainingSummary() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());

        TrainerEntity trainer = trainerRepository.save(new TrainerEntity(
                trainerUsername,
                trainerFirstName,
                trainerLastName,
                true
        ));
        //Save three same trainings
        trainingRepository.saveAll(List.of(
                new TrainingEntity(
                        trainingDate,
                        trainingDuration,
                        trainer
                ),
                new TrainingEntity(
                        trainingDate,
                        trainingDuration,
                        trainer
                ),
                new TrainingEntity(
                        trainingDate,
                        trainingDuration,
                        trainer
                )
        ));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/trainer-workload/" + trainerUsername + "/trainings")
                .contentType("application/JSON")
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        // Get the summary from response
        Integer summaryTrainings = objectMapper.readTree(response.getResponse().getContentAsString())
                .get("trainings")
                .get(String.valueOf(trainingDate.getYear()))
                .get(String.valueOf(trainingDate.getMonthValue()))
                .asInt();

        assertEquals(trainingDuration * 3, summaryTrainings);
    }

    @Test
    void givenNonExistingTrainerUsername_whenGetTrainingsSummary_thenReturn4xx() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/trainer-workload/NonExistingTrainer/trainings")
                .contentType("application/JSON")
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void givenEmptyRequiredField_whenCreateTraining_thenReturn4xx() throws Exception {

        CreateTrainingRequest request1 = new CreateTrainingRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/trainer-workload/")
                        .contentType("application/JSON")
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

}
