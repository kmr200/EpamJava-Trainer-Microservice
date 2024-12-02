package com.epam.xstack.gym.trainer.config.message;

import com.epam.xstack.gym.trainer.dto.request.trainer.UpdateTrainerRequest;
import com.epam.xstack.gym.trainer.dto.request.training.ModifyTrainingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper); // Use your configured ObjectMapper
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type"); // Type header for deserialization

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(
                "com.epam.xstack.gym.dto.request.client.TrainerWorkloadManageTrainingRequest",
                ModifyTrainingRequest.class);
        typeIdMappings.put(
                "com.epam.xstack.gym.dto.request.client.UpdateMicroserviceTrainerRequest",
                UpdateTrainerRequest.class
        );

        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonJmsMessageConverter
    ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJmsMessageConverter); // Set Jackson converter
        factory.setSessionTransacted(true); // Enable transactions
        factory.setConcurrency("1-3"); // 1 to 3 concurrency
        return factory;
    }

}
