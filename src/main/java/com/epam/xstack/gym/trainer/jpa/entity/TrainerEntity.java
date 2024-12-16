package com.epam.xstack.gym.trainer.jpa.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "trainer")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainerEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private Boolean status;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Map<Integer, Map<Integer, Integer>> trainingSummary;

    public TrainerEntity(String trainerUsername, String firstName, String lastName, Boolean status) {
        this.trainerUsername = trainerUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

}
