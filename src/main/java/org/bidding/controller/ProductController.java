package org.bidding.controller;

import org.bidding.dto.ProductDTO;
import org.bidding.model.Product;
import org.bidding.model.Vendor;
import org.bidding.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.bidding.repository.VendorRepository;
import org.bidding.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private VendorRepository vendorRepository;

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ProductDTO productDTO = convertToDTO(product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Vendor vendor = vendorRepository.findById(productDTO.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBasePrice(productDTO.getBasePrice());
        product.setStartTime(productDTO.getStartTime());
        product.setEndTime(productDTO.getEndTime());
        product.setVendor(vendor);

        Product savedProduct = productService.save(product);

        ProductDTO savedProductDTO = convertToDTO(savedProduct);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    // Helper method to convert Product entity to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategory(product.getCategory());
        productDTO.setBasePrice(product.getBasePrice());
        // Add more fields as needed
        return productDTO;
    }

    // Helper method to convert ProductDTO to Product entity
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBasePrice(productDTO.getBasePrice());
        return product;
    }
}