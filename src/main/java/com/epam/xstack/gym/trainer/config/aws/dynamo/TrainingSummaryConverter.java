package com.epam.xstack.gym.trainer.config.aws.dynamo;

import com.epam.xstack.gym.trainer.dto.TrainingSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class TrainingSummaryConverter implements AttributeConverter<TrainingSummary> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AttributeValue transformFrom(TrainingSummary trainingSummary) {
        try {
            // Serialize TrainingSummary object to JSON string
            String json = objectMapper.writeValueAsString(trainingSummary);
            return AttributeValue.builder().s(json).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize TrainingSummary", e);
        }
    }

    @Override
    public TrainingSummary transformTo(AttributeValue attributeValue) {
        try {
            // Deserialize the JSON string back to TrainingSummary object
            return objectMapper.readValue(attributeValue.s(), TrainingSummary.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize TrainingSummary", e);
        }
    }

    @Override
    public EnhancedType<TrainingSummary> type() {
        return EnhancedType.of(TrainingSummary.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
