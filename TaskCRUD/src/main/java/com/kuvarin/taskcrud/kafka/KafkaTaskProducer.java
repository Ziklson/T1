package com.kuvarin.taskcrud.kafka;

import com.kuvarin.taskcrud.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskDTO> template;

    public void sendTaskUpdate(TaskDTO taskDTO) {
        try {
            Message<TaskDTO> message = MessageBuilder.withPayload(taskDTO).build();
            template.send(message);
        } catch (Exception e) {
            log.error("Something went wrong during sending message to KAFKA");
        }
    }

}
