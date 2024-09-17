package org.bidding.service;

import org.bidding.database.entity.BidEntity;
import org.bidding.dto.BidDTO;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    List<BidDTO> findAllBids();
    BidDTO findBidsByID(Long id);
    BidDTO addBidtoDB(BidDTO bid);
    BidDTO updateHigherBids(Long id, BidDTO bid);
    List<BidDTO> findByProductId(Long productId);
    BidDTO placeBid(Long productId, Long userId, BigDecimal bidAmount);
    void endAuction(Long productId);
    BidDTO getCurrentHighestBidForProduct(Long productId);
}