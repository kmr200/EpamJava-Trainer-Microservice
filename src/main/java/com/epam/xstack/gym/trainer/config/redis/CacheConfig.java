package com.epam.xstack.gym.trainer.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@Profile("!test && !integration-test")
public class CacheConfig {

    @Value("${spring.data.redis.ttl:3}")
    private Integer ttl;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Create a custom ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        // Configure the ObjectMapper
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Configure the PolymorphicTypeValidator to allow only specific packages
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.epam.xstack.gym.trainer.dto")
                .allowIfSubType("java.util")
                .allowIfSubType("java.time") // For LocalDate, etc.
                .build();

        // Enable default typing with the PolymorphicTypeValidator
        objectMapper.activateDefaultTyping(
                ptv,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // Create the Jackson2JsonRedisSerializer with the custom ObjectMapper
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // Use StringRedisSerializer for keys
        RedisSerializationContext.SerializationPair<String> keySerializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());

        // Configure the serialization pair for values
        RedisSerializationContext.SerializationPair<Object> valueSerializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(serializer);

        // Create the cache configuration
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializationPair)
                .serializeValuesWith(valueSerializationPair)
                .entryTtl(Duration.ofMinutes(ttl))
                .disableCachingNullValues();

        // Build and return the RedisCacheManager
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration).build();
    }
}
