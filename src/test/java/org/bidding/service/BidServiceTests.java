package org.bidding.service;

import org.bidding.database.adapter.BidAdapter;
import org.bidding.database.adapter.ProductAdapter;
import org.bidding.database.adapter.UserAdapter;
import org.bidding.domain.dto.BidDTO;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.dto.UserDTO;
import org.bidding.service.implementation.BidServiceImpl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BidServiceTests {

    @Mock
    private ProductAdapter productAdapter;

    @Mock
    private UserAdapter userAdapter;

    @InjectMocks
    private BidServiceImpl bidService;

    @Mock
    private BidAdapter bidAdapter;

    public BidServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceBidSuccess() {
        Long productId = 1L;
        Long userId = 1L;
        BigDecimal bidAmount = new BigDecimal("100.00");

        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setBasePrice(new BigDecimal("50.00"));

        UserDTO user = new UserDTO();
        user.setId(userId);

        when(productAdapter.findById(productId)).thenReturn(product);
        when(userAdapter.findById(userId)).thenReturn(user);

        BidDTO bid = new BidDTO();
        bid.setProduct(product);
        bid.setUser(user);
        bid.setAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());

        when(bidAdapter.save(any())).thenReturn(bid);

        BidDTO result = bidService.placeBid(product, user, bidAmount);

        assertNotNull(result);
        assertEquals(bidAmount, result.getAmount());
    }

    @Test
    void testPlaceBidFailureDueToPrice() {
        Long productId = 1L;
        Long userId = 1L;
        BigDecimal bidAmount = new BigDecimal("40.00");

        ProductDTO product = new ProductDTO();
        product.setId(productId);
        product.setBasePrice(new BigDecimal("50.00"));

        UserDTO user = new UserDTO();
        user.setId(userId);

        when(productAdapter.findById(productId)).thenReturn(product);
        when(userAdapter.findById(userId)).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.placeBid(product, user, bidAmount);
        });

        assertEquals("Bid amount must be greater than or equal to the base price", exception.getMessage());
    }
}