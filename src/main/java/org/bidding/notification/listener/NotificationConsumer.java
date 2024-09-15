package org.bidding.notification.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listen(String message) {
        // Process the received notification message
        System.out.println("Received notification: " + message);
        // Here you can add logic to send notifications via email, SMS, etc.
    }
}