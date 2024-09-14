package org.bidding.repository;

import org.bidding.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    // Custom query methods (if needed) can be defined here
    boolean existsByContactInfo(String contactInfo);

}