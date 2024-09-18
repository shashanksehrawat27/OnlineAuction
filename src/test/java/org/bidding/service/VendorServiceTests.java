package org.bidding.service;


import org.bidding.database.adapter.VendorAdapter;
import org.bidding.domain.dto.VendorDTO;
import org.bidding.exception.CannotCreateDuplicateEntryException;
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
    private VendorAdapter vendorAdapter;

    @Test
    public void testSaveVendorWithUniqueContactInfo() {
        VendorDTO vendor = new VendorDTO();
        vendor.setEmailId("unique@example.com");

        when(vendorAdapter.existsByEmailId("unique@example.com")).thenReturn(false);
        when(vendorAdapter.save(any())).thenReturn(vendor);

        VendorDTO savedVendor = vendorService.addVendor(vendor);
        assertEquals(vendor, savedVendor);
    }

    @Test
    public void testSaveVendorWithDuplicateContactInfo() {
        VendorDTO vendor = new VendorDTO();
        vendor.setEmailId("duplicate@example.com");

        when(vendorAdapter.existsByEmailId("duplicate@example.com")).thenReturn(true);

        assertThrows(CannotCreateDuplicateEntryException.class, () -> {
            vendorService.addVendor(vendor);
        });
    }
}