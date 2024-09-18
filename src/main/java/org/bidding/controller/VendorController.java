package org.bidding.controller;

import org.bidding.database.mapper.EntityMapper;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.dto.VendorDTO;
import org.bidding.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private EntityMapper entityMapper;

    // Get all vendors
    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        List<VendorDTO> vendors = vendorService.findAllRegisteredVendors();
        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }

    // Get a vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long id) {
        VendorDTO vendor = vendorService.findVendorByVendorId(id);
        if (vendor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    // Create a new vendor
    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO savedVendor = vendorService.addVendor(vendorDTO);
        return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByVendorId(@PathVariable Long id) {
        List<ProductDTO> products = vendorService.getProductsByVendorId(id);
        if (products == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}