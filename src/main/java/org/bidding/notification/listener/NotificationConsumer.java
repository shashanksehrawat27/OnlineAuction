package org.bidding.notification.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listen(String message) {
        log.info("Received notification: " + message);
    }
}