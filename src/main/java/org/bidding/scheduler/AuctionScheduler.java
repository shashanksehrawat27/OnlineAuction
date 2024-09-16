package org.bidding.scheduler;

import org.bidding.service.BidService;
import org.bidding.database.repository.ProductRepository;
import org.bidding.database.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionScheduler {

    @Autowired
    private BidService bidService;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 300000) // Check every minute
    public void checkAndEndAuctions() {
        List<ProductEntity> products = productRepository.findAll();
        for (ProductEntity product : products) {
            if (LocalDateTime.now().isAfter(product.getEndTime())) {
                bidService.endAuction(product.getId());
            }
        }
    }
}