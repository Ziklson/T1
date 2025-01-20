package com.kuvarin.taskcrud.service;


import com.kuvarin.taskcrud.config.MailConfig;
import com.kuvarin.taskcrud.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    private final MailConfig mailConfig;


    public void sendNotification(List<TaskDTO> messages) {
        for (TaskDTO message: messages) {
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setFrom(mailConfig.getNoReply());
            emailMessage.setTo(mailConfig.getDefaultRecipient());
            emailMessage.setSubject("Task Status Updated");
            emailMessage.setText(message.toString());
            mailSender.send(emailMessage);
        }
    }


}
