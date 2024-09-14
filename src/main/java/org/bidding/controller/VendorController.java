package org.bidding.controller;

import org.bidding.dto.VendorDTO;
import org.bidding.model.Vendor;
import org.bidding.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // Get all vendors
    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        List<Vendor> vendors = vendorService.findAll();
        List<VendorDTO> vendorDTOs = vendors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(vendorDTOs, HttpStatus.OK);
    }

    // Get a vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long id) {
        Vendor vendor = vendorService.findById(id);
        if (vendor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        VendorDTO vendorDTO = convertToDTO(vendor);
        return new ResponseEntity<>(vendorDTO, HttpStatus.OK);
    }

    // Create a new vendor
    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorDTO vendorDTO) {
        Vendor vendor = convertToEntity(vendorDTO);
        Vendor savedVendor = vendorService.save(vendor);
        VendorDTO savedVendorDTO = convertToDTO(savedVendor);
        return new ResponseEntity<>(savedVendorDTO, HttpStatus.CREATED);
    }

    // Helper method to convert Vendor entity to VendorDTO
    private VendorDTO convertToDTO(Vendor vendor) {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setId(vendor.getId());
        vendorDTO.setName(vendor.getName());
        vendorDTO.setContactInfo(vendor.getContactInfo());
        // Add more fields as needed
        return vendorDTO;
    }

    // Helper method to convert VendorDTO to Vendor entity
    private Vendor convertToEntity(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();
        vendor.setName(vendorDTO.getName());
        vendor.setContactInfo(vendorDTO.getContactInfo());
        // Add more fields as needed
        return vendor;
    }
}