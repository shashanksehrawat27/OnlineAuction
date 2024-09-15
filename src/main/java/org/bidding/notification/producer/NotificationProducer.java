package org.bidding.notification.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "notifications";

    public void sendNotification(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}