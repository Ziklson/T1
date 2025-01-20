package com.kuvarin.taskcrud.kafka;

import com.kuvarin.starter.aspect.annotation.LogException;
import com.kuvarin.taskcrud.dto.TaskDTO;
import com.kuvarin.taskcrud.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskConsumer {

    private final NotificationService notificationService;


    @LogException
    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}",
    topics = "${spring.kafka.consumer.topic-name}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskDTO> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {

        log.info("Task consumer: Обработка новых сообщений");

        try {
            notificationService.sendNotification(messageList);
            log.info("Task consumer: Идёт обработка ");
        } catch (Exception e) {
            log.error("Something went wrong during sending notification: {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }

        log.info("Task consumer: Записи обработаны");

    }

}
