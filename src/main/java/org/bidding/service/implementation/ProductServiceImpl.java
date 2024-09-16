package org.bidding.service.implementation;

import jakarta.validation.ConstraintViolationException;
import org.bidding.database.entity.BidEntity;
import org.bidding.database.entity.VendorEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.dto.ProductDTO;
import org.bidding.database.entity.ProductEntity;
import org.bidding.notification.producer.NotificationProducer;
import org.bidding.database.repository.ProductRepository;
import org.bidding.database.repository.VendorRepository;
import org.bidding.service.NotificationService;
import org.bidding.service.ProductService;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.bidding.database.repository.BidRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationProducer notificationProducer;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<ProductDTO> findAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream().map(entityMapper::toProductDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO findProductByProductId(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return entityMapper.toProductDTO(product);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        List<ProductEntity> products = productRepository.findByCategory(category);
        return products.stream().map(entityMapper::toProductDTO).collect(Collectors.toList());
    }


    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        // Validate and retrieve the vendor
        VendorEntity vendor = vendorRepository.findById(productDTO.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor with ID " + productDTO.getVendorId() + " not found"));

        // Convert ProductDTO to ProductEntity
        ProductEntity product = entityMapper.toProduct(productDTO);

        // Set the vendor for the product
        product.setVendor(vendor);

        try {
            // Save the product
            ProductEntity savedProduct = productRepository.save(product);

            // Add the product to the vendor's product list
            vendor.getProducts().add(savedProduct);
            vendorRepository.save(vendor); // Save the vendor to update the product list

            return entityMapper.toProductDTO(savedProduct);
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
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductEntity productToUpdate = entityMapper.toProduct(productDTO);
        productToUpdate.setId(existingProduct.getId());

        ProductEntity updatedProduct = productRepository.save(productToUpdate);
        return entityMapper.toProductDTO(updatedProduct);
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void endAuctionIfSlotEnded(ProductEntity product) {
        if (LocalDateTime.now().isAfter(product.getEndTime())) {
            BidEntity winningBid = bidRepository.findTopByProductIdOrderByAmountDesc(product.getId());

            if (winningBid != null) {
                notificationService.sendAuctionEndNotification(product.getId(), winningBid.getUser().getId());
            } else {
                notificationService.sendNoBidsNotification(product.getId());
            }

            String message = "Auction for product ID " + product.getId() + " has ended.";
            notificationProducer.sendNotification(message);
        }
    }
}