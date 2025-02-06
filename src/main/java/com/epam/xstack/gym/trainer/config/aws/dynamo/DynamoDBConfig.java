package com.epam.xstack.gym.trainer.config.aws.dynamo;

import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTemplate dynamoDbTemplate(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return new DynamoDbTemplate(dynamoDbEnhancedClient);
    }

    @Bean
    public DynamoDbTable<TrainerEntity> trainerTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("trainer-workload", TableSchema.fromBean(TrainerEntity.class));
    }

}
