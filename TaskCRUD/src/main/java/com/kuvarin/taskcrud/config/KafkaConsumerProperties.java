package com.kuvarin.taskcrud.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperties {

    private String bootstrapServers;
    private int maxPollRecords;
    private String autoOffsetReset;
    private String groupId;
    private boolean enableAutoCommit;

    private String keyDeserializer;
    private String valueDeserializer;

    private Map<String, Object> properties;


}
