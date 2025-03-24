package com.epam.xstack.gym.trainer.jpa.repository;

import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
public class TrainerRepository {
    private final DynamoDbTable<TrainerEntity> trainerTable;

    public TrainerRepository(DynamoDbTable<TrainerEntity> trainerTable) {
        this.trainerTable = trainerTable;
    }

    public Optional<TrainerEntity> findByTrainerUsernameIgnoreCase(String trainerUsername) {
        return Optional.ofNullable(trainerTable.getItem(
                Key.builder().partitionValue(trainerUsername.toLowerCase()).build()
        ));
    }

    public TrainerEntity save(TrainerEntity trainer) {
        trainerTable.putItem(trainer);
        return trainer;
    }

    public void delete(TrainerEntity trainer) {
        trainerTable.deleteItem(
                Key.builder().partitionValue(trainer.getTrainerUsername().toLowerCase()).build()
        );
    }

    public Optional<TrainerEntity> findById(String trainerUsername) {
        return Optional.ofNullable(trainerTable.getItem(
                Key.builder().partitionValue(trainerUsername.toLowerCase()).build()
        ));
    }
}
