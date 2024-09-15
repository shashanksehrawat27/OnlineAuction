package org.bidding.service.implementation;

import org.bidding.service.NotificationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendBidNotification(Long userId, Long productId, BigDecimal bidAmount) {
        // Implement the logic to send a bid notification to the user
    }

    @Override
    public void sendAuctionEndNotification(Long productId, Long userId) {
        // Implement the logic to notify the user that they have won the auction
    }

    @Override
    public void sendNoBidsNotification(Long productId) {
        // Implement the logic to notify that no bids were received
    }
}