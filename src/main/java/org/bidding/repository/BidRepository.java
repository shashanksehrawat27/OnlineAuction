package org.bidding.repository;

import org.bidding.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    // Custom query methods (if needed) can be defined here
}