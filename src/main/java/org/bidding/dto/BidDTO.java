package org.bidding.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidDTO {
    private Long id;
    private Long productId;
    private Long userId;
    private BigDecimal amount;
    private LocalDateTime bidTime;

    // Constructors
    public BidDTO() {}

    public BidDTO(Long id, Long productId, Long userId, BigDecimal amount, LocalDateTime bidTime) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.amount = amount;
        this.bidTime = bidTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
}