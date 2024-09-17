package org.bidding.service;

import org.bidding.database.entity.ProductEntity;
import org.bidding.dto.ProductDTO;
import org.bidding.dto.VendorDTO;

import java.util.List;

public interface VendorService {
    List<VendorDTO> findAllRegisteredVendors(); // Fetch all vendors
    VendorDTO findVendorByVendorId(Long id); // Find a vendor by its ID
    VendorDTO addVendor(VendorDTO vendor); // Save a new vendor
    List<ProductDTO> getProductsByVendorId(Long vendorId);
    VendorDTO updateVendorDetails(Long id, VendorDTO vendor); // Update an existing vendor
    boolean deleteVendor(Long id); // Delete a vendor by its ID
}