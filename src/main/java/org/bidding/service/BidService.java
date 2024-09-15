package org.bidding.service;

import org.bidding.model.Bid;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    List<Bid> findAll();
    Bid findById(Long id);
    Bid save(Bid bid);
    Bid update(Long id, Bid bid);
    boolean delete(Long id);
    List<Bid> findByProductId(Long productId);
    Bid placeBid(Long productId, Long userId, BigDecimal bidAmount);

    void endAuction(Long productId);
}