package com.kuvarin.taskcrud.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class KafkaProducerProperties {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String topic;
    private boolean enableIdempotence;

}
