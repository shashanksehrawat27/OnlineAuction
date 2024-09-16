package org.bidding.service.implementation;

import org.bidding.database.entity.BidEntity;
import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.UserEntity;
import org.bidding.exception.NoBidsFoundException;
import org.bidding.notification.producer.NotificationProducer;
import org.bidding.database.repository.BidRepository;
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
    private BidRepository bidRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<BidEntity> findAllBids() {
        return bidRepository.findAll();
    }

    @Override
    public BidEntity findBidsByID(Long id) {
        return bidRepository.findById(id).orElse(null);
    }

    @Override
    public BidEntity addBidtoDB(BidEntity bid) {
        return bidRepository.save(bid);
    }

    @Override
    public BidEntity updateHigherBids(Long id, BidEntity bid) {
        if (bidRepository.existsById(id)) {
            bid.setId(id);
            return bidRepository.save(bid);
        }
        return null;
    }

    @Override
    public BidEntity getCurrentHighestBidForProduct(Long productId) {
        BidEntity highestBid = bidRepository.findTopByProductIdOrderByAmountDesc(productId);
        if (highestBid == null) {
            throw new NoBidsFoundException("No bids found for product ID " + productId);
        }
        return highestBid;
    }
    @Override
    public List<BidEntity> findByProductId(Long productId) {
        return bidRepository.findByProductId(productId);
    }

    @Override
    public BidEntity placeBid(Long productId, Long userId, BigDecimal bidAmount) {
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
        BidEntity highestBid = bidRepository.findTopByProductIdOrderByAmountDesc(productId);

        // Ensure the new bid amount is higher than or equal to the current highest bid
        if (highestBid != null && bidAmount.compareTo(highestBid.getAmount()) < 0) {
            throw new IllegalArgumentException("Bid amount must be higher than or equal to the current highest bid.");
        }

        // Ensure the bid amount is greater than or equal to the base price
        if (bidAmount.compareTo(product.getBasePrice()) < 0) {
            throw new IllegalArgumentException("Bid amount must be greater than or equal to the base price.");
        }

        // Create and save the bid
        BidEntity bid = new BidEntity();
        bid.setProduct(product);
        bid.setUser(user);
        bid.setAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());

        BidEntity savedBid = bidRepository.save(bid);

        // Send bid notification to the user
        notificationService.sendBidNotification(userId, productId, bidAmount);

        // Handle auction end after placing the bid if necessary
        endAuctionIfSlotEnded(product);

        return savedBid;
    }

    private void endAuctionIfSlotEnded(ProductEntity product) {
        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            // Handle end of auction logic
            BidEntity winningBid = bidRepository.findTopByProductIdOrderByAmountDesc(product.getId());

            if (winningBid != null) {
                notificationService.sendAuctionEndNotification(product.getId(), winningBid.getUser().getId());
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

        BidEntity winningBid = bidRepository.findTopByProductIdOrderByAmountDesc(productId);

        if (winningBid != null) {
            notificationService.sendAuctionEndNotification(productId, winningBid.getUser().getId());
        } else {
            notificationService.sendNoBidsNotification(productId);
        }

        // Delete the product after auction ends
        productRepository.delete(product);

        String message = "Auction for product ID " + productId + " has ended.";
        notificationProducer.sendNotification(message);
    }
}