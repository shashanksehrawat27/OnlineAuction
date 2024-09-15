package org.bidding.service;

import java.math.BigDecimal;

public interface NotificationService {
    void sendBidNotification(Long userId, Long productId, BigDecimal bidAmount);
    void sendAuctionEndNotification(Long productId, Long userId);
    void sendNoBidsNotification(Long productId);
}