package org.bidding.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class BidDTO {
    private Long id;
    private ProductDTO product;
    private UserDTO user;
    private BigDecimal amount;
    private LocalDateTime bidTime;

    // Constructors
    public BidDTO() {}

    public BidDTO(Long id, ProductDTO product, UserDTO user, BigDecimal amount, LocalDateTime bidTime) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.amount = amount;
        this.bidTime = bidTime;
    }
}