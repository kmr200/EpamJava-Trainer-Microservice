import com.epam.xstack.gym.trainer.TrainerWorkloadServiceApplication;
import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
public class TrainerControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TrainingRepository trainingRepository;

    @MockBean
    CacheManager cacheManager;

    private final String trainerUsername = "trainer";
    private final String trainerFirstName = "John";
    private final String trainerLastName = "Doe";
    private final String contentType = "application/json";
    private final LocalDate trainingDate = LocalDate.now();
    private final Integer trainingDuration = 30;

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
                .get("/api/v1/trainer-workload/trainer/" + trainerUsername + "/trainings")
                .contentType(contentType)
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
                .get("/api/v1/trainer-workload/trainer/NonExistingTrainer/trainings")
                .contentType(contentType)
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void givenNonExistingTrainerUsername_whenUpdateTrainer_thenReturn4xx() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/trainer-workload/trainer/NonExistingTrainer/")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(new UpdateTrainerRequest()))
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}
