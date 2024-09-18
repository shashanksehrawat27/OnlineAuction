package org.bidding.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.bidding.database.entity.ProductEntity;
import org.bidding.domain.enums.ProductCategoryEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private BigDecimal basePrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long vendorId; // Reference to the Vendor

    // Constructors
    public ProductDTO() {}

    public ProductDTO(Long id, String name, String category, BigDecimal basePrice, LocalDateTime startTime, LocalDateTime endTime, Long vendorId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.vendorId = vendorId;
    }
}