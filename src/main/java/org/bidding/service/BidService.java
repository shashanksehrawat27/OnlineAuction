package org.bidding.service;

import org.bidding.database.entity.BidEntity;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    List<BidEntity> findAllBids();
    BidEntity findBidsByID(Long id);
    BidEntity addBidtoDB(BidEntity bid);
    BidEntity updateHigherBids(Long id, BidEntity bid);
    List<BidEntity> findByProductId(Long productId);
    BidEntity placeBid(Long productId, Long userId, BigDecimal bidAmount);
    void endAuction(Long productId);
    BidEntity getCurrentHighestBidForProduct(Long productId);
}