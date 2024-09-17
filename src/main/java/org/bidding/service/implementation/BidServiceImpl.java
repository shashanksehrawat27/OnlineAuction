package org.bidding.service.implementation;

import org.bidding.database.adapter.BidAdapter;
import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.UserEntity;
import org.bidding.dto.BidDTO;
import org.bidding.exception.NoBidsFoundException;
import org.bidding.notification.producer.NotificationProducer;
import org.bidding.database.repository.ProductRepository;
import org.bidding.database.repository.UserRepository;
import org.bidding.service.BidService;
import org.bidding.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bidding.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BidAdapter bidAdapter;

    @Override
    public List<BidDTO> findAllBids() {
        return bidAdapter.findAll();
    }

    @Override
    public BidDTO findBidsByID(Long id) {
        return bidAdapter.findById(id);
    }

    @Override
    public BidDTO addBidtoDB(BidDTO bid) {
        return bidAdapter.save(bid);
    }

    @Override
    public BidDTO updateHigherBids(Long id, BidDTO bid) {
        if (bidAdapter.existsById(id)) {
            bid.setId(id);
            return bidAdapter.save(bid);
        }
        return null;
    }

    @Override
    public BidDTO getCurrentHighestBidForProduct(Long productId) {
        BidDTO highestBid = bidAdapter.findTopByProductIdOrderByAmountDesc(productId);
        if (highestBid == null) {
            throw new NoBidsFoundException("No bids found for product ID " + productId);
        }
        return highestBid;
    }
    @Override
    public List<BidDTO> findByProductId(Long productId) {
        return bidAdapter.findByProductId(productId);
    }

    @Override
    public BidDTO placeBid(Long productId, Long userId, BigDecimal bidAmount) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Ensure the auction is still ongoing
        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            endAuction(productId);
            throw new IllegalStateException("The auction for this product has already ended.");
        }

        // Get the current highest bid for the product
        BidDTO highestBid = bidAdapter.findTopByProductIdOrderByAmountDesc(productId);

        // Ensure the new bid amount is higher than or equal to the current highest bid
        if (highestBid != null && bidAmount.compareTo(highestBid.getAmount()) < 0) {
            throw new IllegalArgumentException("Bid amount must be higher than or equal to the current highest bid.");
        }

        // Ensure the bid amount is greater than or equal to the base price
        if (bidAmount.compareTo(product.getBasePrice()) < 0) {
            throw new IllegalArgumentException("Bid amount must be greater than or equal to the base price.");
        }

        // Create and save the bid
        BidDTO bid = new BidDTO();
        bid.setProductId(product.getId());
        bid.setUserId(user.getId());
        bid.setAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());

        BidDTO savedBid = bidAdapter.save(bid);

        // Send bid notification to the user
        notificationService.sendBidNotification(userId, productId, bidAmount);

        // Handle auction end after placing the bid if necessary
        endAuctionIfSlotEnded(product);

        return savedBid;
    }

    private void endAuctionIfSlotEnded(ProductEntity product) {
        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            // Handle end of auction logic
            BidDTO winningBid = bidAdapter.findTopByProductIdOrderByAmountDesc(product.getId());

            if (winningBid != null) {
                notificationService.sendAuctionEndNotification(product.getId(), winningBid.getUserId());
            } else {
                notificationService.sendNoBidsNotification(product.getId());
            }

            // Notify that the auction has ended
            String message = "Auction for product ID " + product.getId() + " has ended.";
            notificationProducer.sendNotification(message);
        }
    }

    @Override
    public void endAuction(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (LocalDateTime.now().isBefore(product.getEndTime())) {
            throw new IllegalStateException("Auction has not ended yet.");
        }

        BidDTO winningBid = bidAdapter.findTopByProductIdOrderByAmountDesc(productId);

        if (winningBid != null) {
            notificationService.sendAuctionEndNotification(productId, winningBid.getUserId());
        } else {
            notificationService.sendNoBidsNotification(productId);
        }

        // Delete the product after auction ends
        productRepository.delete(product);

        String message = "Auction for product ID " + productId + " has ended.";
        notificationProducer.sendNotification(message);
    }
}