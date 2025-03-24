package com.epam.xstack.gym.trainer.config.aws.dynamo;

import com.epam.xstack.gym.trainer.config.aws.AwsProperties;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.stereotype.Component;

@Component
public class TableNameResolver implements DynamoDbTableNameResolver {

    private final AwsProperties awsProperties;

    public TableNameResolver(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @Override
    public <T> String resolve(Class<T> clazz) {
        return awsProperties.getDynamoTableName();
    }
}
