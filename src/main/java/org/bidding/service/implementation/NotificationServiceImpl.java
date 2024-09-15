package org.bidding.service.implementation;

import org.bidding.service.NotificationService;
import org.bidding.notification.producer.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationProducer notificationProducer;

    @Override
    public void sendBidNotification(Long userId, Long productId, BigDecimal bidAmount) {
        String message = "User " + userId + " has placed a bid of " + bidAmount + " on product " + productId + ".";
        notificationProducer.sendNotification(message);
    }

    @Override
    public void sendAuctionEndNotification(Long productId, Long userId) {
        String message = "Auction for product " + productId + " has ended. User " + userId + " has won the auction.";
        notificationProducer.sendNotification(message);
    }

    @Override
    public void sendNoBidsNotification(Long productId) {
        String message = "Auction for product " + productId + " has ended with no bids.";
        notificationProducer.sendNotification(message);
    }
}