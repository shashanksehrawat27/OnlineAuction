package org.bidding.controller;

import org.bidding.database.entity.BidEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.dto.BidDTO;
import org.bidding.dto.ProductDTO;
import org.bidding.exception.ResourceNotFoundException;
import org.bidding.service.BidService;
import org.bidding.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BidService bidService;
    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.findProductByProductId(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(
            @RequestParam("category") String category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/highest-bid")
    public ResponseEntity<BidDTO> getCurrentHighestBidForProduct(@PathVariable Long id) {
        BidEntity highestBid = bidService.getCurrentHighestBidForProduct(id);
        if (highestBid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BidDTO bidDTO = entityMapper.toBidDTO(highestBid);
        return new ResponseEntity<>(bidDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            validateProductDTO(productDTO);
            ProductDTO createdProduct = productService.addProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getVendorId() == null) {
            throw new IllegalArgumentException("Vendor ID must not be null");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProductDetail(id, productDTO);
        if (updatedProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}