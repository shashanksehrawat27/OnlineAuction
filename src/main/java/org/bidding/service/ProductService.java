package org.bidding.service;

import org.bidding.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAllProducts();
    ProductDTO findProductByProductId(Long id);
    List<ProductDTO> getProductsByCategory(String category);
    ProductDTO addProduct(ProductDTO productDTO);
    ProductDTO updateProductDetail(Long id, ProductDTO productDTO);
    boolean deleteProduct(Long id);
}