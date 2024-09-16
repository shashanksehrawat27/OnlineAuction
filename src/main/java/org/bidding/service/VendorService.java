package org.bidding.service;

import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.VendorEntity;

import java.util.List;

public interface VendorService {
    List<VendorEntity> findAllRegisteredVendors(); // Fetch all vendors
    VendorEntity findVendorByVendorId(Long id); // Find a vendor by its ID
    VendorEntity addVendor(VendorEntity vendor); // Save a new vendor
    List<ProductEntity> getProductsByVendorId(Long vendorId);
    VendorEntity updateVendorDetails(Long id, VendorEntity vendor); // Update an existing vendor
    boolean deleteVendor(Long id); // Delete a vendor by its ID
}