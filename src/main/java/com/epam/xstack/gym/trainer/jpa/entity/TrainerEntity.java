package com.epam.xstack.gym.trainer.jpa.entity;

import com.epam.xstack.gym.trainer.config.aws.dynamo.TrainingSummaryConverter;
import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import lombok.*;
import org.springframework.data.annotation.Id;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@DynamoDbBean
@Setter
public class TrainerEntity {

    @Id
    private String id;

    private String trainerUsername;
    private String firstName;
    private String lastName;
    private Boolean status;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TrainingSummary trainingSummary;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("trainerUsername")
    public String getTrainerUsername() {
        return trainerUsername;
    }

    @DynamoDbAttribute("firstName")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDbAttribute("lastName")
    public String getLastName() {
        return lastName;
    }

    @DynamoDbAttribute("status")
    public Boolean getStatus() {
        return status;
    }

    @DynamoDbAttribute("trainingSummary")
    @DynamoDbConvertedBy(TrainingSummaryConverter.class)
    public TrainingSummary getTrainingSummary() {
        return trainingSummary;
    }

    public TrainerEntity(String trainerUsername, String firstName, String lastName, Boolean status) {
        this.trainerUsername = trainerUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

}
