package org.bidding.database.repository;

import org.bidding.database.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorEntity, Long> {
    boolean existsByEmailId(String emailId);

}