package org.bidding.service.implementation;

import org.bidding.database.entity.ProductEntity;
import org.bidding.database.entity.VendorEntity;
import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.database.repository.VendorRepository;
import org.bidding.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public List<VendorEntity> findAllRegisteredVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public VendorEntity findVendorByVendorId(Long id) {
        return vendorRepository.findById(id).orElse(null);
    }

    @Override
    public VendorEntity addVendor(VendorEntity vendor) {

        if (vendorRepository.existsByEmailId(vendor.getEmailId())){
            throw new CannotCreateDuplicateEntryException("Vendor already exists in DB.");
        }
        return vendorRepository.save(vendor);
    }

    @Override
    public List<ProductEntity> getProductsByVendorId(Long vendorId) {
        VendorEntity vendor = vendorRepository.findById(vendorId).orElse(null);
        return vendor != null ? vendor.getProducts() : null;
    }
    @Override
    public VendorEntity updateVendorDetails(Long id, VendorEntity vendor) {
        if (vendorRepository.existsById(id)) {
            vendor.setId(id);
            return vendorRepository.save(vendor);
        }
        return null;
    }

    @Override
    public boolean deleteVendor(Long id) {
        if (vendorRepository.existsById(id)) {
            vendorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}