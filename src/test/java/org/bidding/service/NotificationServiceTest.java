package org.bidding.service;

import org.bidding.service.NotificationService;
import org.bidding.service.implementation.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

public class NotificationServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    public NotificationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendBidNotification() {
        Long userId = 1L;
        Long productId = 2L;
        BigDecimal bidAmount = BigDecimal.valueOf(100.00);

        notificationService.sendBidNotification(userId, productId, bidAmount);

        String expectedMessage = String.format("User %d placed a bid of %s on product %d", userId, bidAmount, productId);
        verify(kafkaTemplate).send("notifications", expectedMessage);
    }

    @Test
    public void testSendAuctionEndNotification() {
        Long productId = 2L;
        Long userId = 1L;

        notificationService.sendAuctionEndNotification(productId, userId);

        String expectedMessage = String.format("Auction for product %d ended. Winner: User %d", productId, userId);
        verify(kafkaTemplate).send("notifications", expectedMessage);
    }

    @Test
    public void testSendNoBidsNotification() {
        Long productId = 2L;

        notificationService.sendNoBidsNotification(productId);

        String expectedMessage = String.format("No bids were placed for product %d", productId);
        verify(kafkaTemplate).send("notifications", expectedMessage);
    }
}