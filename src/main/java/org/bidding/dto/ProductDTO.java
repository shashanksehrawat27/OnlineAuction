package org.bidding.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}