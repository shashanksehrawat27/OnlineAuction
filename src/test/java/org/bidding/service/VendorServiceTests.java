package org.bidding.service;


import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.model.Vendor;
import org.bidding.repository.VendorRepository;
import org.bidding.service.VendorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        Vendor vendor = new Vendor();
        vendor.setContactInfo("unique@example.com");

        when(vendorRepository.existsByContactInfo("unique@example.com")).thenReturn(false);
        when(vendorRepository.save(vendor)).thenReturn(vendor);

        Vendor savedVendor = vendorService.save(vendor);
        assertEquals(vendor, savedVendor);
    }

    @Test
    public void testSaveVendorWithDuplicateContactInfo() {
        Vendor vendor = new Vendor();
        vendor.setContactInfo("duplicate@example.com");

        when(vendorRepository.existsByContactInfo("duplicate@example.com")).thenReturn(true);

        assertThrows(CannotCreateDuplicateEntryException.class, () -> {
            vendorService.save(vendor);
        });
    }
}
