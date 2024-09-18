package org.bidding.service.implementation;

import org.bidding.database.adapter.BidAdapter;
import org.bidding.database.adapter.ProductAdapter;
import org.bidding.database.adapter.VendorAdapter;
import org.bidding.domain.dto.BidDTO;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.database.entity.ProductEntity;
import org.bidding.domain.dto.VendorDTO;
import org.bidding.domain.enums.ProductCategoryEnum;
import org.bidding.notification.producer.NotificationProducer;
import org.bidding.service.NotificationService;
import org.bidding.service.ProductService;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
//        productDTO.setVendorId(vendor.getId());

        ProductDTO savedProduct = productAdapter.save(productDTO);

        vendor.getProducts().add(savedProduct);
        vendorAdapter.save(vendor);

        return savedProduct;
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
                notificationService.sendAuctionEndNotification(product.getId(), winningBid.getUser().getId());
            } else {
                notificationService.sendNoBidsNotification(product.getId());
            }

            String message = "Auction for product ID " + product.getId() + " has ended.";
            notificationProducer.sendNotification(message);
        }
    }
}