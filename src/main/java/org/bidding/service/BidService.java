package org.bidding.service;

import org.bidding.domain.dto.BidDTO;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.dto.UserDTO;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    List<BidDTO> findAllBids();
    BidDTO findBidsByID(Long id);
    BidDTO addBidtoDB(BidDTO bid);
    BidDTO updateHigherBids(Long id, BidDTO bid);
    List<BidDTO> findByProductId(Long productId);
    BidDTO placeBid(ProductDTO productId, UserDTO userId, BigDecimal bidAmount);
    void endAuction(Long productId);
    BidDTO getCurrentHighestBidForProduct(Long productId);
}