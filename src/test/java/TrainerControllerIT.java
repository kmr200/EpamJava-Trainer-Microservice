import com.epam.xstack.gym.trainer.TrainerWorkloadServiceApplication;
import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import com.epam.xstack.gym.trainer.jpa.repository.TrainerRepository;
import com.epam.xstack.gym.trainer.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TrainerWorkloadServiceApplication.class)
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@AutoConfigureMockMvc
public class TrainerControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrainerService trainerService;

    @MockBean
    private TrainerRepository trainerRepositoryMock;

    private static final String BASE_URL = "/api/v1/trainer-workload/trainer";

    private static final String USERNAME = "michael.wilson";
    private static final String FIRSTNAME = "Michael";
    private static final String LASTNAME = "Wilson";
    private static final TrainerEntity trainerEntity = new TrainerEntity(
            USERNAME,
            FIRSTNAME,
            LASTNAME,
            true);

    @Test
    public void givenValidUsername_whenGetTrainings_thenReturnTrainingsResponse() throws Exception {
        LocalDate today = LocalDate.now();
        trainerEntity.setTrainingSummary(
                new TrainingSummary()
                        .addTraining(today, 30)
        );
        when(trainerRepositoryMock.findByTrainerUsernameIgnoreCase(USERNAME)).thenReturn(
                Optional.of(trainerEntity)
        );

        mockMvc.perform(get(BASE_URL + "/" + USERNAME + "/trainings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trainerUsername").value(USERNAME))
                .andExpect(jsonPath("$.trainerFirstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.trainerLastName").value(LASTNAME))
                .andExpect(jsonPath("$.trainerStatus").value(true))
                .andExpect(jsonPath("$.trainings." + today.getYear() + "." + today.getMonthValue()).value(30));
    }

    @Test
    public void givenInvalidUsername_whenGetTrainings_thenReturnNotFound() throws Exception {
        when(trainerRepositoryMock.findByTrainerUsernameIgnoreCase(Mockito.anyString())).thenReturn(Optional.empty());
        mockMvc.perform(get(BASE_URL + "/invalid_user/trainings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}