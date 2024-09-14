package org.bidding.service;

import org.bidding.model.Vendor;

import java.util.List;

public interface VendorService {
    List<Vendor> findAll(); // Fetch all vendors
    Vendor findById(Long id); // Find a vendor by its ID
    Vendor save(Vendor vendor); // Save a new vendor
    Vendor update(Long id, Vendor vendor); // Update an existing vendor
    boolean delete(Long id); // Delete a vendor by its ID
}