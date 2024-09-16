package org.bidding.database.repository;

import org.bidding.database.entity.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, Long> {
    List<BidEntity> findByProductId(Long productId);
    BidEntity findTopByProductIdOrderByAmountDesc(Long productId);
}

