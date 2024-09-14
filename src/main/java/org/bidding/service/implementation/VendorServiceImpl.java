package org.bidding.service.implementation;

import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.model.Vendor;
import org.bidding.repository.VendorRepository;
import org.bidding.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public List<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor findById(Long id) {
        return vendorRepository.findById(id).orElse(null);
    }

    @Override
    public Vendor save(Vendor vendor) {

        if (vendorRepository.existsByContactInfo(vendor.getContactInfo())){
            throw new CannotCreateDuplicateEntryException("Vendor already exists in DB.");
        }
        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor update(Long id, Vendor vendor) {
        if (vendorRepository.existsById(id)) {
            vendor.setId(id);
            return vendorRepository.save(vendor);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (vendorRepository.existsById(id)) {
            vendorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}