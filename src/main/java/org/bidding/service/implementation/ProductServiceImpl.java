package org.bidding.service.implementation;

import org.bidding.dto.ProductDTO;
import org.bidding.model.Product;
import org.bidding.repository.ProductRepository;
import org.bidding.repository.VendorRepository;
import org.bidding.service.ProductService;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);

        // You can check for constraints like duplicate entries here before saving
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Product productToUpdate = convertToEntity(productDTO);
        productToUpdate.setId(existingProduct.getId()); // Ensure you're updating the correct product

        Product updatedProduct = productRepository.save(productToUpdate);
        return convertToDTO(updatedProduct);
    }

    @Override
    public boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategory(product.getCategory());
        productDTO.setBasePrice(product.getBasePrice());
        productDTO.setStartTime(product.getStartTime());
        productDTO.setEndTime(product.getEndTime());
        if (product.getVendor() != null) {
            productDTO.setVendorId(product.getVendor().getId());
        }
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBasePrice(productDTO.getBasePrice());
        product.setStartTime(productDTO.getStartTime());
        product.setEndTime(productDTO.getEndTime());
        if (productDTO.getVendorId() != null) {
            product.setVendor(vendorRepository.findById(productDTO.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found")));
        }
        return product;
    }
}