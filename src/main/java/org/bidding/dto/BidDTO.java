package org.bidding.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
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
}