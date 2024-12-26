package com.epam.xstack.gym.trainer.jpa.entity;

import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trainer")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainerEntity {

    @Id
    private String id;

    @Indexed(name = "unique_trainer_username_idx", unique = true)
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private Boolean status;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TrainingSummary trainingSummary;

    public TrainerEntity(String trainerUsername, String firstName, String lastName, Boolean status) {
        this.trainerUsername = trainerUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }



}
