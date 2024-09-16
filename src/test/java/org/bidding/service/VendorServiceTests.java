package org.bidding.service;


import org.bidding.database.entity.VendorEntity;
import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.database.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
public class VendorServiceTests {

    @Autowired
    private VendorService vendorService;

    @MockBean
    private VendorRepository vendorRepository;

    @Test
    public void testSaveVendorWithUniqueContactInfo() {
        VendorEntity vendor = new VendorEntity();
        vendor.setEmailId("unique@example.com");

        when(vendorRepository.existsByEmailId("unique@example.com")).thenReturn(false);
        when(vendorRepository.save(vendor)).thenReturn(vendor);

        VendorEntity savedVendor = vendorService.addVendor(vendor);
        assertEquals(vendor, savedVendor);
    }

    @Test
    public void testSaveVendorWithDuplicateContactInfo() {
        VendorEntity vendor = new VendorEntity();
        vendor.setEmailId("duplicate@example.com");

        when(vendorRepository.existsByEmailId("duplicate@example.com")).thenReturn(true);

        assertThrows(CannotCreateDuplicateEntryException.class, () -> {
            vendorService.addVendor(vendor);
        });
    }
}
