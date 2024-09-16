package org.bidding.service;

import org.bidding.database.entity.BidEntity;
import org.bidding.service.implementation.BidServiceImpl;

import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.UserEntity;
import org.bidding.database.repository.BidRepository;
import org.bidding.database.repository.ProductRepository;
import org.bidding.database.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BidServiceTests {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    public BidServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceBidSuccess() {
        Long productId = 1L;
        Long userId = 1L;
        BigDecimal bidAmount = new BigDecimal("100.00");

        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setBasePrice(new BigDecimal("50.00"));

        UserEntity user = new UserEntity();
        user.setId(userId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        BidEntity bid = new BidEntity();
        bid.setProduct(product);
        bid.setUser(user);
        bid.setAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());

        when(bidRepository.save(any(BidEntity.class))).thenReturn(bid);

        BidEntity result = bidService.placeBid(productId, userId, bidAmount);

        assertNotNull(result);
        assertEquals(bidAmount, result.getAmount());
    }

    @Test
    void testPlaceBidFailureDueToPrice() {
        Long productId = 1L;
        Long userId = 1L;
        BigDecimal bidAmount = new BigDecimal("40.00");

        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setBasePrice(new BigDecimal("50.00"));

        UserEntity user = new UserEntity();
        user.setId(userId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.placeBid(productId, userId, bidAmount);
        });

        assertEquals("Bid amount must be greater than or equal to the base price", exception.getMessage());
    }
}