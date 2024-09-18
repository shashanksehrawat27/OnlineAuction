package org.bidding.service.implementation;

import org.bidding.database.adapter.VendorAdapter;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.dto.VendorDTO;
import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorAdapter vendorAdapter;

    @Override
    public List<VendorDTO> findAllRegisteredVendors() {
        return vendorAdapter.findAll();
    }

    @Override
    public VendorDTO findVendorByVendorId(Long id) {
        return vendorAdapter.findById(id);
    }

    @Override
    public VendorDTO addVendor(VendorDTO vendor) {

        if (vendorAdapter.existsByEmailId(vendor.getEmailId())){
            throw new CannotCreateDuplicateEntryException("Vendor already exists in DB.");
        }
        return vendorAdapter.save(vendor);
    }

    @Override
    public List<ProductDTO> getProductsByVendorId(Long vendorId) {
        VendorDTO vendor = vendorAdapter.findById(vendorId);
        return vendor != null ? vendor.getProducts() : null;
    }
    @Override
    public VendorDTO updateVendorDetails(Long id, VendorDTO vendor) {
        if (vendorAdapter.existsById(id)) {
            vendor.setId(id);
            return vendorAdapter.save(vendor);
        }
        return null;
    }

    @Override
    public boolean deleteVendor(Long id) {
        if (vendorAdapter.existsById(id)) {
            vendorAdapter.deleteById(id);
            return true;
        }
        return false;
    }
}