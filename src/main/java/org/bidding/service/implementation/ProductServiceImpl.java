package org.bidding.service.implementation;

import jakarta.validation.ConstraintViolationException;
import org.bidding.database.adapter.BidAdapter;
import org.bidding.database.adapter.ProductAdapter;
import org.bidding.database.adapter.VendorAdapter;
import org.bidding.dto.BidDTO;
import org.bidding.dto.ProductDTO;
import org.bidding.database.entity.ProductEntity;
import org.bidding.dto.VendorDTO;
import org.bidding.notification.producer.NotificationProducer;
import org.bidding.service.NotificationService;
import org.bidding.service.ProductService;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductAdapter productAdapter;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private BidAdapter bidAdapter;
    @Autowired
    private VendorAdapter vendorAdapter;

    @Override
    public List<ProductDTO> findAllProducts() {
        return productAdapter.findAll();
    }

    @Override
    public ProductDTO findProductByProductId(Long id) {
        return productAdapter.findById(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        return productAdapter.findByCategory(category);
    }


    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        // Validate and retrieve the vendor
        VendorDTO vendor = vendorAdapter.findById(productDTO.getVendorId());
        if(vendor == null) {
            throw new ResourceNotFoundException("Vendor with ID " + productDTO.getVendorId() + " not found");
        }

        // Set the vendor for the product
        productDTO.setVendorId(vendor.getId());

        try {
            // Save the product
            ProductDTO savedProduct = productAdapter.save(productDTO);

            // Add the product to the vendor's product list
            vendor.getProducts().add(savedProduct);
            vendorAdapter.save(vendor); // Save the vendor to update the product list

            return savedProduct;
        } catch (DataIntegrityViolationException e) {
            // Handle specific database integrity constraint violations
            throw new RuntimeException("Data integrity violation: " + e.getMessage(), e);
        } catch (ConstraintViolationException e) {
            // Handle constraint violations
            throw new RuntimeException("Constraint violation: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle general exceptions
            throw new RuntimeException("Error occurred while saving the product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDTO updateProductDetail(Long id, ProductDTO productDTO) {
        ProductDTO productToUpdate = productAdapter.findById(id);
        return productAdapter.save(productToUpdate);
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productAdapter.existsById(id)) {
            productAdapter.deleteById(id);
            return true;
        }
        return false;
    }

    private void endAuctionIfSlotEnded(ProductEntity product) {
        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            BidDTO winningBid = bidAdapter.findTopByProductIdOrderByAmountDesc(product.getId());

            if (winningBid != null) {
                notificationService.sendAuctionEndNotification(product.getId(), winningBid.getUserId());
            } else {
                notificationService.sendNoBidsNotification(product.getId());
            }

            String message = "Auction for product ID " + product.getId() + " has ended.";
            notificationProducer.sendNotification(message);
        }
    }
}